package com.linkedin.replica.connections.database.handlers;

import com.linkedin.replica.connections.models.UserInFriendsList;

import java.util.ArrayList;

public interface FriendsListHandler extends DatabaseHandler{

    /**
     * Returns user's friends list
     *
     * @param userID
     */
    UserInFriendsList[] getFriendsList(String userID);
}