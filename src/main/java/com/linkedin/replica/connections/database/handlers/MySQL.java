package com.linkedin.replica.connections.database.handlers;

import com.linkedin.replica.connections.database.handlers.DatabaseHandler;

import java.sql.SQLException;

public abstract class MySQL implements DatabaseHandler{
    /**
     * Block a user
     * User with userID1 blocks user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    public void blockUser(String userID1, String userID2) throws SQLException {

    }

    /**
     * Unblock a user
     * User with userID1 unblocks user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    public void unBlockUser(String userID1, String userID2) throws SQLException {

    }

    /**
     * Add user a friend
     * User with userID1 adds user with userID2 as friend
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    public void addFriend(String userID1, String userID2) throws SQLException {

    }
}
