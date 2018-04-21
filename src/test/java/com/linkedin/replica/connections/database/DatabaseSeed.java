package com.linkedin.replica.connections.database;

import com.arangodb.ArangoDB;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.handlers.impl.MySqlHandler;
import com.linkedin.replica.connections.models.User;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DatabaseSeed {
    Connection mySqlConnection;
    BufferedReader br;
    MySqlHandler mySqlHandler;
    ArangoDB arangoDB;
    String dbName;
    String collectionName;
    public DatabaseSeed() throws SQLException, IOException, ClassNotFoundException {
        mySqlConnection = DatabaseConnection.getInstance().getMysqlConn();
        br = new BufferedReader(new FileReader("src/test/java/resources/users"));
        mySqlHandler = new MySqlHandler();

        arangoDB = DatabaseConnection.getInstance().getArangodb();
        dbName = Configuration.getInstance().getArangoConfigProp("db.name");
        collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");
    }

    public void insertUsers() throws IOException, SQLException {
        ArrayList<String> ids = new ArrayList<String>();
        ids.add("111");
        ids.add("222");
        ids.add("333");
        while(br.ready()){
            StringTokenizer st = new StringTokenizer(br.readLine());
            String userID = st.nextToken();
            String email = st.nextToken();
            String password = st.nextToken();
            String query = "{CALL insert_user(?,?,?)}";
            CallableStatement stmt = mySqlConnection.prepareCall(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, userID);
            stmt.executeQuery();

            User user = new User();
            if(userID.equals("111") || userID.equals("222") || userID.equals("333")){
                ArrayList<String> friendsList = new ArrayList<>(ids);
                friendsList.remove(userID);
                user.setFriendsList(friendsList);
            }
            user.setUserId(userID);
            arangoDB.db(dbName).collection(collectionName).insertDocument(user);
        }
    }

    public void insertFriendRequest() throws SQLException {
        String userID1 = "54f8a99f-0f23-4bdc-a899-90480d7d4032";
        String userID2 = "da7b6939-0f5c-404c-b317-8e5d21b05204";
        int v = userID1.compareTo(userID2);
        if (v > 0) {
            String temp = userID1;
            userID1 = userID2;
            userID2 = temp;
        }
        String query = "{CALL Add_Friend(?, ?, ?)}";
        CallableStatement stmt = mySqlConnection.prepareCall(query);
        stmt.setString(1, userID1);
        stmt.setString(2, userID2);
        stmt.setInt(3, 0);
        stmt.executeQuery();
    }

}
