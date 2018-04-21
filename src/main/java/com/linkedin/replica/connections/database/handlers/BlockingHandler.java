package com.linkedin.replica.connections.database.handlers;

import com.linkedin.replica.connections.database.handlers.DatabaseHandler;

import java.sql.SQLException;

public abstract class BlockingHandler implements DatabaseHandler{
    /**
     * Block a user
     * User with userID1 blocks user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    public abstract void blockUser(String userID1, String userID2) throws SQLException;

    /**
     * Unblock a user
     * User with userID1 unblocks user with userID2
     *
     * @param userID1
     * @param userID2
     * @throws SQLException
     */
    public abstract void unBlockUser(String userID1, String userID2) throws SQLException;


}
