package com.linkedin.replica.connections.database.handlers.impl;

import com.arangodb.ArangoDB;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.DatabaseConnections;
import com.linkedin.replica.connections.database.handlers.GetFriendsList;
import com.linkedin.replica.connections.models.User;
import com.linkedin.replica.connections.models.UserInFriendsList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetFriendsListHandler extends GetFriendsList  {
    private ArangoDB arangoDB;
    private String dbName;

    public GetFriendsListHandler() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
        arangoDB = DatabaseConnections.getInstance().getArangodb();
        dbName = Configuration.getInstance().getArangoConfigProp("db.name");
    }

    /**
     * Returns user's friends list
     *
     * @param userID
     */
    @Override
    public ArrayList<UserInFriendsList> getFriendsList(String userID) {
        String collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");

        User user1 = arangoDB.db(dbName).collection(collectionName).getDocument(userID, User.class);
        return user1.getFriendsList();
    }
}

