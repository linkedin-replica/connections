package com.linkedin.replica.connections.database.handlers;

import com.linkedin.replica.connections.models.UserInFriendsList;

import java.sql.SQLException;

public interface FriendsHandler extends DatabaseHandler {
    /**
     * Accept a friend request
     * User with userID1 accepts request from user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    void acceptFriendRequest(String userID1, String userID2) throws SQLException;

    /**
     * Unfriend a user
     * User with userID1 unfriends user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    void unfriendUser(String userID1, String userID2) throws SQLException;

    /**
     * Add user a friend
     * User with userID1 adds user with userID2 as friend
     *
     * @param userID1
     */
    public abstract UserInFriendsList[] getFriendRequests(String userID1) throws SQLException;

    /**
     * Add user a friend
     * User with userID1 adds user with userID2 as friend
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    void addFriend(String userID1, String userID2) throws SQLException;


    String getUserName(String userId);
}
