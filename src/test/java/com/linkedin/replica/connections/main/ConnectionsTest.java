package com.linkedin.replica.connections.main;

import com.linkedin.replica.connections.databaseHandlers.DatabaseSeed;
import com.linkedin.replica.connections.config.DatabaseConnections;
import org.junit.*;
import com.linkedin.replica.connections.services.ConnectionsService;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class ConnectionsTest {
    private static ConnectionsService service;
    static Connection mySqlConnection;

    @BeforeClass
    public static void setup() throws SQLException, IOException, ClassNotFoundException {
        String[] args = {"src/main/resources/database_config", "src/main/resources/command_config" , "src/main/resources/arango_names"};
        Connections.start(args);
        service = new ConnectionsService();
        DatabaseSeed dbseed = new DatabaseSeed();
        dbseed.insertUsers();
        mySqlConnection = DatabaseConnections.getInstance().getMysqlConn();

    }

    @Test
    public void testAddFriend() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String user1ID = "e4def870-f331-4fb5-a44c-967592cf5b42"; //anwar
        String user2ID = "ff810a3f-07fc-4d35-bc84-98aed333b043"; // hatem
        String commandName = "addFriend";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userID1", user1ID);
        parameters.put("userID2", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_friends_with_user where user1_id = \"" + user1ID + "\" and user2_id = \"" + user2ID + "\" and is_accepted = " + 0;
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;

        assertTrue(size != 0);
    }

    @Test
    public void testAcceptFriendRequest() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String user1ID = "e4def870-f331-4fb5-a44c-967592cf5b42";
        String user2ID = "ff810a3f-07fc-4d35-bc84-98aed333b043";
        String commandName = "acceptFriend";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userID1", user1ID);
        parameters.put("userID2", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_friends_with_user where user1_id = \"" + user1ID + "\" and user2_id = \"" + user2ID + "\" and is_accepted = " + 2;
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size != 0);
    }

    @Test
    public void testBlockUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String user1ID = "064ff6df-63e2-456c-9d18-4184073d7a6d"; //esraa
        String user2ID = "55f4ebbb-606e-4e49-9604-830491c17d73"; //baher
        String commandName = "blockUser";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userID1", user1ID);
        parameters.put("userID2", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_blocked_user where blocking_user_id = \"" + user1ID + "\" and blocked_user_id = \"" + user2ID + "\"";
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size != 0);
    }

    @Test
    public void testUnblockUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String user1ID = "064ff6df-63e2-456c-9d18-4184073d7a6d"; //esraa
        String user2ID = "55f4ebbb-606e-4e49-9604-830491c17d73"; //baher
        String commandName = "unblockUser";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userID1", user1ID);
        parameters.put("userID2", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_blocked_user where blocking_user_id = \"" + user1ID + "\" and blocked_user_id = \"" + user2ID + "\"";
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size == 0);
    }

    @Test
    public void testUnfriendUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String user1ID = "e4def870-f331-4fb5-a44c-967592cf5b42";
        String user2ID = "ff810a3f-07fc-4d35-bc84-98aed333b043";
        String commandName = "unfriendUser";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userID1", user1ID);
        parameters.put("userID2", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_friends_with_user where user1_id = \"" + user1ID + "\" and user2_id = \"" + user2ID + "\"";
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size == 0);
    }

    @AfterClass
    public static void clean() throws SQLException {
        String query = "delete from user_friends_with_user";
        Statement statement = mySqlConnection.createStatement();
        statement.executeUpdate(query);

        query = "delete from user_blocked_user";
        statement = mySqlConnection.createStatement();
        statement.executeUpdate(query);

        query = "delete from users";
        statement = mySqlConnection.createStatement();
        statement.executeUpdate(query);
    }
}
