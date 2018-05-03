package com.linkedin.replica.connections.database.handlers;

import com.linkedin.replica.connections.database.handlers.DatabaseHandler;

import java.sql.SQLException;

public interface BlockingHandler extends DatabaseHandler{
    /**
     * Block a user
     * User with userID1 blocks user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    void blockUser(String userID1, String userID2) throws SQLException;

    /**
     * Unblock a user
     * User with userID1 unblocks user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    void unBlockUser(String userID1, String userID2) throws SQLException;

    /**
     * ignore a friend request from a user
     * User with userID1 ignores request from user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    void ignoreRequest(String userID1, String userID2) throws SQLException;


}
