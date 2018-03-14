package databaseHandlers;

import java.sql.SQLException;

public interface DatabaseHandler {
	/**
	 * Block a user 
	 * User with userID1 blocks user with userID2
	 * @throws SQLException 
	 */
	public void blockUser(String userID1,String userID2) throws SQLException;
	/**
	 * Unblock a user 
	 * User with userID1 unblocks user with userID2
	 * @throws SQLException 
	 */
	public void unBlockUser(String userID1,String userID2) throws SQLException;
	/**
	 * Add user a friend 
	 * User with userID1 adds user with userID2 as friend
	 * @throws SQLException 
	 */
	public void addFriend(String userID1,String userID2) throws SQLException;
	
	/**
	 * Accept friend request 
	 * User with userID1 accepts friend request user with userID2
	 * @throws SQLException 
	 */
	public void acceptFriendRequest(String userID1,String userID2) throws SQLException;
	
	/**
	 * Unfriend a user 
	 * User with userID1 unfriends user with userID2
	 * @throws SQLException 
	 */
	public void unfriendUser(String userID1,String userID2) throws SQLException;
	
}
