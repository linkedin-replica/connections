package com.linkedin.replica.connections.database.handlers;

import java.sql.SQLException;

public abstract class AddingRemovingFriends implements DatabaseHandler {
    /**
     * Accept a friend request
     * User with userID1 accepts request from user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    public abstract void acceptFriendRequest(String userID1, String userID2) throws SQLException;

    /**
     * Unfriend a user
     * User with userID1 unfriends user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    public abstract void unfriendUser(String userID1, String userID2) throws SQLException;
}
