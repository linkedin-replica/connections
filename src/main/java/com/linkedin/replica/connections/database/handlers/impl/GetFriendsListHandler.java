package com.linkedin.replica.connections.database.handlers.impl;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.DatabaseConnection;
import com.linkedin.replica.connections.database.handlers.GetFriendsList;
import com.linkedin.replica.connections.models.User;
import com.linkedin.replica.connections.models.UserInFriendsList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetFriendsListHandler extends GetFriendsList  {
    private ArangoDB arangoDB;
    private String dbName;

    public GetFriendsListHandler() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
        arangoDB = DatabaseConnection.getInstance().getArangodb();
        dbName = Configuration.getInstance().getArangoConfigProp("db.name");
    }

    /**
     * Returns user's friends list
     *
     * @param userID
     */
    @Override
    public UserInFriendsList[] getFriendsList(String userID) {
        String collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");
        String query = "For u IN " + collectionName + " FILTER u.userId == @id LET result = ( FOR u2 in "+ collectionName + " FILTER u2.userId in u.friendsList return {userId: u2.userId, firstName : u2.firstName, lastName : u2.lastName, profilePictureUrl : u2.profilePictureUrl} ) return result";
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("id", userID);
        ArangoCursor<UserInFriendsList[]> cursor = arangoDB.db(dbName).query(query, bindVars, null, UserInFriendsList[].class);

        UserInFriendsList[] ret = cursor.next();
        for (int i = 0; i < ret.length; i++) {
            System.out.println(ret[i].toString());
        }
        return ret;
    }

}

