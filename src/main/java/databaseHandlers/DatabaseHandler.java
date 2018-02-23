package main.java.databaseHandlers;

public interface DatabaseHandler {
	/**
	 * Block a user 
	 * User with userID1 blocks user with userID2
	 */
	public void blockUser(String userID1,String userID2);
	
	/**
	 * Add user a friend 
	 * User with userID1 adds user with userID2 as friend
	 */
	public void addFriend(String userID1,String userID2);
	
	/**
	 * Accept friend request 
	 * User with userID1 accepts friend request user with userID2
	 */
	public void acceptFriendRequest(String userID1,String userID2);
	
	/**
	 * Unfriend a user 
	 * User with userID1 unfriends user with userID2
	 */
	public void unfriendUser(String userID1,String userID2);
	
}
