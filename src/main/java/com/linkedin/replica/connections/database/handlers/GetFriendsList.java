package com.linkedin.replica.connections.database.handlers;

import com.linkedin.replica.connections.models.UserInFriendsList;

import java.util.ArrayList;

public abstract class GetFriendsList implements DatabaseHandler{

    /**
     * Returns user's friends list
     *
     * @param userID
     */
    public abstract UserInFriendsList[] getFriendsList(String userID);
}