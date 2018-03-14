package com.linkedin.replica.connections.main;

import com.linkedin.replica.connections.databaseHandlers.DatabaseSeed;
import main.java.config.DatabaseConnections;
import main.java.services.ConnectionsService;
import main.java.main.Connections;

import org.junit.*;

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
        String query = "select * from user_friends_with_user where user1_id = \"" + user1ID + "\" and user2_id = " + user2ID + " and is_accepted = " + 0;
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;

        assertTrue(size != 0);
    }

    @Test
    public void testAcceptFriendRequest() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String user1ID = "U1";
        String user2ID = "U2";
        String commandName = "acceptFriend";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userID1", user1ID);
        parameters.put("userID2", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_friends_with_user where user1_id = " + user1ID + " and user2_id = " + user2ID + " and is_accepted = " + 2;
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size != 0);
    }

    @Test
    public void testBlockUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String user1ID = "U1";
        String user2ID = "U2";
        String commandName = "blockUser";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userID1", user1ID);
        parameters.put("userID2", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_blocked_user where user1_id = " + user1ID + " and user2_id = " + user2ID;
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size != 0);
    }

    @Test
    public void testUnblockUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String user1ID = "U1";
        String user2ID = "U2";
        String commandName = "unblockUser";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userID1", user1ID);
        parameters.put("userID2", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_blocked_user where user1_id = " + user1ID + " and user2_id = " + user2ID;
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size == 0);
    }

    @Test
    public void testUnfriendUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String user1ID = "U1";
        String user2ID = "U2";
        String commandName = "unfriendUser";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userID1", user1ID);
        parameters.put("userID2", user2ID);

        service.serve(commandName, parameters);
        String query = "select * from user_friends_with_user where user1_id = " + user1ID + " and user2_id = " + user2ID;
        Statement statement = mySqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int size = 0;
        while(resultSet.next())
            size++;
        assertTrue(size == 0);
    }

    @AfterClass
    public static void clean() throws SQLException {
        String query = "truncate user_friends_with_user";
        Statement statement = mySqlConnection.createStatement();
        statement.executeUpdate(query);

        query = "truncate users";
        statement = mySqlConnection.createStatement();
        statement.executeUpdate(query);
    }
}
