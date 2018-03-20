package com.linkedin.replica.connections.databaseHandlers;

import com.linkedin.replica.connections.config.DatabaseConnections;
import com.linkedin.replica.connections.databaseHandlers.impl.MySqlHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class DatabaseSeed {
    Connection mySqlConnection;
    BufferedReader br;
    MySqlHandler mySqlHandler;

    public DatabaseSeed() throws SQLException, IOException, ClassNotFoundException {
        mySqlConnection = DatabaseConnections.getInstance().getMysqlConn();
        br = new BufferedReader(new FileReader("src/test/java/resources/users"));
        mySqlHandler = new MySqlHandler();
    }

    public void insertUsers() throws IOException, SQLException {
        while(br.ready()){
            StringTokenizer st = new StringTokenizer(br.readLine());
            String userID = st.nextToken();
            String email = st.nextToken();
            String password = st.nextToken();
            String query = "{CALL Insert_User(?,?,?)}";
            CallableStatement stmt = mySqlConnection.prepareCall(query);
            stmt.setString(1, userID);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.executeQuery();
        }
    }

}
