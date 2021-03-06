package com.linkedin.replica.connections.main;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.DatabaseConnection;
import com.linkedin.replica.connections.database.DatabaseSeed;
import com.linkedin.replica.connections.database.handlers.BlockingHandler;
import com.linkedin.replica.connections.database.handlers.DatabaseHandler;
import com.linkedin.replica.connections.database.handlers.FriendsHandler;
import com.linkedin.replica.connections.database.handlers.impl.ArangoFriendsListHandler;
import com.linkedin.replica.connections.database.handlers.impl.ArangoMySQLFriendsHandler;
import com.linkedin.replica.connections.database.handlers.impl.MySQLBlockingHandler;
import com.linkedin.replica.connections.models.UserInFriendsList;

import com.linkedin.replica.connections.messaging.SendNotificationHandler;
import com.linkedin.replica.connections.services.ConnectionsService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;

public class MainTest {
    private static ConnectionsService service;
    private static Connection mySqlConnection;
    private static ArangoDB arangoDB;
    private static Configuration config;
    private static String dbName, userCollectionName;

    @BeforeClass
    public static void setup() throws SQLException, IOException, ClassNotFoundException, TimeoutException {
        String[] args = {"src/main/resources/app.config",
                "src/main/resources/database.test.config" ,
                "src/main/resources/commands.config",
                "src/main/resources/controller.config"};
        Configuration.init(args[0], args[1], args[1], args[2], args[3]);
        DatabaseConnection.init();
        SendNotificationHandler.init();
        service = new ConnectionsService();
        DatabaseSeed dbseed = new DatabaseSeed();
        mySqlConnection = DatabaseConnection.getInstance().getMysqlConn();
        arangoDB = DatabaseConnection.getInstance().getArangodb();
        config = Configuration.getInstance();
        dbName = config.getArangoConfigProp("db.name");
        userCollectionName = config.getArangoConfigProp("collection.users.name");
        dbseed.insertUsers();
        dbseed.insertFriendRequest();
    }

    @Test
    public void testAddFriend() throws ClassNotFoundException, SQLException,
            InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        String user1ID = "e4def870-f331-4fb5-a44c-967592cf5b42"; //anwar
        String user2ID = "ff810a3f-07fc-4d35-bc84-98aed333b043"; // hatem
        String commandName = "connections.addFriend";
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userId", user1ID);
        parameters.put("userId1", user2ID);
        service.serve(commandName, parameters);
        String query = "select * from user_friends_with_user where user1_id = \""
                        + user1ID + "\" and user2_id = \"" + user2ID + "\" and is_accepted = " + 0;
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;

        assertTrue(size != 0);
    }

    @Test
    public void testAcceptFriendRequest() throws ClassNotFoundException, SQLException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        String user1ID = "54f8a99f-0f23-4bdc-a899-90480d7d4032"; //nada
        String user2ID = "da7b6939-0f5c-404c-b317-8e5d21b05204"; //yara
        int v = user1ID.compareTo(user2ID);
        if(v > 0) {
            String temp = user1ID;
            user1ID = user2ID;
            user2ID = temp;
        }
        String commandName = "connections.acceptFriend";
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userId", user1ID);
        parameters.put("userId1", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_friends_with_user where user1_id = \"" + user1ID + "\" and user2_id = \""
                                            + user2ID + "\" and is_accepted = " + 2;
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size != 0);
        query = "FOR u IN " + userCollectionName + " FILTER u.userId == @id return u.friendsList";
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("id", user1ID);
        ArangoCursor cursor = arangoDB.db(dbName).query(query, bindVars, null, String.class);
        size = 0;
        while(cursor.hasNext()) {
            String curr = (String) cursor.next();
            if(curr.trim().equals("") || curr.trim().isEmpty() || curr.trim().equals("[]"))
                continue;
            size++;
        }
        assertTrue(size != 0);

        query = "FOR u IN " + userCollectionName + " FILTER u.userId == @id return u.friendsList";
        bindVars.put("id", user2ID);
        cursor = arangoDB.db(dbName).query(query, bindVars, null, String.class);
        size = 0;
        while(cursor.hasNext()) {
            String curr = (String) cursor.next();
            if(curr.trim().equals("") || curr.trim().isEmpty() || curr.trim().equals("[]"))
                continue;
            size++;
        }
        assertTrue(size != 0);

    }

    @Test
    public void testBlockUser() throws ClassNotFoundException, SQLException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        String user1ID = "064ff6df-63e2-456c-9d18-4184073d7a6d"; //esraa
        String user2ID = "55f4ebbb-606e-4e49-9604-830491c17d73"; //baher
        String commandName = "connections.blockUser";
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userId", user1ID);
        parameters.put("userId1", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_blocked_user where blocking_user_id = \""
                                        + user1ID + "\" and blocked_user_id = \"" + user2ID + "\"";
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size != 0);
    }

    @Test
    public void testUnblockUser() throws ClassNotFoundException, SQLException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        String user1ID = "064ff6df-63e2-456c-9d18-4184073d7a6d"; //esraa
        String user2ID = "55f4ebbb-606e-4e49-9604-830491c17d73"; //baher
        String commandName = "connections.unblockUser";
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userId", user1ID);
        parameters.put("userId1", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_blocked_user where blocking_user_id = \""
                                            + user1ID + "\" and blocked_user_id = \"" + user2ID + "\"";
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size == 0);
    }

    @Test
    public void testUnfriendUser() throws ClassNotFoundException, SQLException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        String user1ID = "e4def870-f331-4fb5-a44c-967592cf5b42";
        String user2ID = "ff810a3f-07fc-4d35-bc84-98aed333b043";
        String commandName = "connections.unfriendUser";
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userId", user1ID);
        parameters.put("userId1", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_friends_with_user where user1_id = \""
                                                + user1ID + "\" and user2_id = \"" + user2ID + "\"";
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size == 0);
        query = "FOR u IN " + userCollectionName + " FILTER u.userId == @id return u.friendsList";
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("id", user1ID);
        ArangoCursor cursor = arangoDB.db(dbName).query(query, bindVars, null, String.class);
        size = 0;
        while(cursor.hasNext()) {
            String curr = (String) cursor.next();
            if(curr.trim().equals("") || curr.trim().isEmpty() || curr.trim().equals("[]"))
                continue;
            size++;
        }
        assertTrue(size == 0);

        query = "FOR u IN " + userCollectionName + " FILTER u.userId == @id return u.friendsList";
        bindVars.put("id", user2ID);
        cursor = arangoDB.db(dbName).query(query, bindVars, null, String.class);
        size = 0;
        while(cursor.hasNext()) {
            String curr = (String) cursor.next();
            if(curr.trim().equals("") || curr.trim().isEmpty() || curr.trim().equals("[]"))
                continue;
            size++;
        }
        assertTrue(size == 0);
    }

    @Test
    public void testGetFriendsList() throws IllegalAccessException, InvocationTargetException,
            InstantiationException, SQLException, NoSuchMethodException, ClassNotFoundException, IOException {
        String commandName = "connections.getFriendsList";
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userId", "111");
        UserInFriendsList[] result = (UserInFriendsList[]) service.serve(commandName, parameters);

        assertTrue(result[0].getUserId().equals("222") || result[0].getUserId().equals("333"));
        assertTrue(result[1].getUserId().equals("222") || result[1].getUserId().equals("333"));
    }

    @Test
    public void testGetFriendRequests() throws IllegalAccessException, InvocationTargetException, InstantiationException, SQLException, NoSuchMethodException, ClassNotFoundException, IOException {
        String user1ID = "111";
        String user2ID = "444";
        String commandName = "connections.addFriend";
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userId", user1ID);
        parameters.put("userId1", user2ID);
        service.serve(commandName, parameters);

        user1ID = "555";
        user2ID = "444";
        HashMap<String, Object> parameters1 = new HashMap<String, Object>();
        parameters1.put("userId", user1ID);
        parameters1.put("userId1", user2ID);
        service.serve(commandName, parameters1);


        commandName = "connections.getFriendRequests";
        HashMap<String, Object> parameters2 = new HashMap<String, Object>();
        parameters2.put("userId", "444");
        UserInFriendsList[] result = (UserInFriendsList[]) service.serve(commandName, parameters2);
        assertTrue(result[0].getUserId().equals("555") || result[0].getUserId().equals("111"));
        assertTrue(result[1].getUserId().equals("555") || result[1].getUserId().equals("111"));
    }

    @Test
    public void testIgnoreFriendRequest() throws SQLException, IOException, ClassNotFoundException {
        String user1ID = "666";
        String user2ID = "777";
        DatabaseHandler dbHandler = new ArangoMySQLFriendsHandler();
        ((FriendsHandler)dbHandler).addFriend(user1ID, user2ID);

        dbHandler =  new MySQLBlockingHandler();
        ((BlockingHandler)dbHandler).ignoreRequest(user2ID, user1ID);

        String query = "SELECT * FROM user_friends_with_user WHERE user1_id = ? AND user2_id = ?;";
        PreparedStatement ps = mySqlConnection.prepareStatement(query);
        ps.setString(1, user1ID);
        ps.setString(2, user2ID);
        ResultSet res = ps.executeQuery();

        int size = 0;
        while(res.next())
            size++;

        assertTrue(size == 0);
    }

    @AfterClass
    public static void clean() throws SQLException, IOException, ClassNotFoundException {
        String query = "delete from user_friends_with_user";
        Statement statement = mySqlConnection.createStatement();
        statement.executeUpdate(query);

        query = "delete from user_blocked_user";
        statement = mySqlConnection.createStatement();
        statement.executeUpdate(query);

        query = "delete from users";
        statement = mySqlConnection.createStatement();
        statement.executeUpdate(query);

        arangoDB.db(dbName).collection(userCollectionName).truncate();
        DatabaseConnection.getInstance().closeConnections();
        SendNotificationHandler.getInstance().closeConnections();
    }


}
