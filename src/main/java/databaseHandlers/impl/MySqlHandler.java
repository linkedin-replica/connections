package main.java.databaseHandlers.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import main.java.config.DatabaseConnections;
import main.java.databaseHandlers.DatabaseHandler;

public class MySqlHandler implements DatabaseHandler {
	
	Connection mySqlConnection;
	
	public MySqlHandler() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException{
		mySqlConnection = DatabaseConnections.getInstance().getMysqlConn();
	}

	@Override
	public void blockUser(String userID1, String userID2) throws SQLException {
		String query = "{CALL Insert_Blocked_User(?,?)}";
		CallableStatement stmt = mySqlConnection.prepareCall(query);
		stmt.setString(1, userID1);
		stmt.setString(2, userID2);
		stmt.executeQuery();
	}

	@Override
	public void unBlockUser(String userID1, String userID2) throws SQLException {
		String query = "{CALL delete_user_blocks_user(?,?)}";
		CallableStatement stmt = mySqlConnection.prepareCall(query);
		stmt.setString(1, userID1);
		stmt.setString(2, userID2);
		stmt.executeQuery();
	}
	
	@Override
	public void addFriend(String userID1, String userID2) throws SQLException {
		String query = "{CALL Insert_Added_Friend(?,?)}";
		CallableStatement stmt = mySqlConnection.prepareCall(query);
		stmt.setString(1, userID1);
		stmt.setString(2, userID2);
		stmt.executeQuery();
	}

	@Override
	public void acceptFriendRequest(String userID1, String userID2) throws SQLException {
		
		// TODO to be modified after creating of stored procedure
		String query = "{CALL Insert_Added_Friend(?,?)}";
		CallableStatement stmt = mySqlConnection.prepareCall(query);
		stmt.setString(1, userID1);
		stmt.setString(2, userID2);
		stmt.executeQuery();
	}

	@Override
	public void unfriendUser(String userID1, String userID2) throws SQLException {
		String query = "{CALL delete_user_friend_with_user(?,?)}";
		CallableStatement stmt = mySqlConnection.prepareCall(query);
		stmt.setString(1, userID1);
		stmt.setString(2, userID2);
		stmt.executeQuery();	
	}


}
