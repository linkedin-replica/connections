package com.linkedin.replica.connections.database.handlers.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

import com.linkedin.replica.connections.database.DatabaseConnection;
import com.linkedin.replica.connections.database.handlers.BlockingHandler;

public class MySQLBlockingHandler implements BlockingHandler {
	
	Connection mySqlConnection;
	
	public MySQLBlockingHandler() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException{
		mySqlConnection = DatabaseConnection.getInstance().getMysqlConn();
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

	public void ignoreRequest(String userID1, String userID2) throws SQLException {
		int isAccepted = 0;
		if(userID1.compareTo(userID2) < 0){
			isAccepted = 1;
		}
		String query = "{CALL delete_friend_request(?,?,?)}";
		PreparedStatement ps = mySqlConnection.prepareStatement(query);
		ps.setString(1, userID1);
		ps.setString(2, userID2);
		ps.setInt(3, isAccepted);
		ResultSet res = ps.executeQuery();
	}

}
