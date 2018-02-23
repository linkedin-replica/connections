package main.java.databaseHandlers;

public interface DatabaseHandler {
	/**
	 * Block a user 
	 */
	public void blockUser(String name);
	
	/**
	 * Add user a friend 
	 */
	public void addFriend(String name);
	
	/**
	 * Accept friend request 
	 */
	public void acceptFriendRequest(String name);
	
	/**
	 * Unfriend a user 
	 */
	public void unfriendUser(String name);
	
}
