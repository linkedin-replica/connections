package com.linkedin.replica.connections.database.handlers.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.linkedin.replica.connections.database.DatabaseConnections;

public class MySqlHandler implements DatabaseHandler {
	
	Connection mySqlConnection;
	
	public MySqlHandler() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException{
		mySqlConnection = DatabaseConnections.getInstance().getMysqlConn();
	}


	public void blockUser(String userID1, String userID2) throws SQLException {
		String query = "{CALL Insert_Blocked_User(?,?)}";
		CallableStatement stmt = mySqlConnection.prepareCall(query);
		stmt.setString(1, userID1);
		stmt.setString(2, userID2);
		stmt.executeQuery();
	}


	public void unBlockUser(String userID1, String userID2) throws SQLException {
		String query = "{CALL delete_user_blocks_user(?,?)}";
		CallableStatement stmt = mySqlConnection.prepareCall(query);
		stmt.setString(1, userID1);
		stmt.setString(2, userID2);
		stmt.executeQuery();
	}
	

	public void addFriend(String userID1, String userID2) throws SQLException {
		int status = -1;
		if(userID1.compareTo(userID2) < 1)
			status = 0; // user 1 adds user 2;
		else{
			String temp = userID1;
			userID1 = userID2;
			userID2 = temp;
			status = 1; // user 2 adds user 1;
		}

		String query = "{CALL Add_Friend(?,?,?)}";
		CallableStatement stmt = mySqlConnection.prepareCall(query);
		stmt.setString(1, userID1);
		stmt.setString(2, userID2);
		stmt.setInt(3, status);
		stmt.executeQuery();
	}


	public void acceptFriendRequest(String userID1, String userID2) throws SQLException {
		if(userID1.compareTo(userID2) > 0){
			String temp = userID1;
			userID1 = userID2;
			userID2 = temp;
		}

		String query = "{CALL accept_friend_request(?,?)}";
		CallableStatement stmt = mySqlConnection.prepareCall(query);
		stmt.setString(1, userID1);
		stmt.setString(2, userID2);
		stmt.executeQuery();
	}


	public void unfriendUser(String userID1, String userID2) throws SQLException {
		String query = "{CALL delete_user_friend_with_user(?,?)}";
		CallableStatement stmt = mySqlConnection.prepareCall(query);
		stmt.setString(1, userID1);
		stmt.setString(2, userID2);
		stmt.executeQuery();	
	}


}
