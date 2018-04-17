package com.linkedin.replica.connections.database.handlers.impl;

import com.arangodb.ArangoDB;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.DatabaseConnections;
import com.linkedin.replica.connections.database.handlers.DatabaseHandler;
import com.linkedin.replica.connections.models.UserInFriendsList;
import com.linkedin.replica.connections.models.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArangoDBHandler implements DatabaseHandler {
    private ArangoDB arangoDB;
    private String dbName;

    public ArangoDBHandler() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
        arangoDB = DatabaseConnections.getInstance().getArangodb();
        dbName = Configuration.getInstance().getArangoConfigProp("db.name");
    }

    public void blockUser(String userID1, String userID2) throws SQLException {

    }

    public void unBlockUser(String userID1, String userID2) throws SQLException {

    }

    public void addFriend(String userID1, String userID2) throws SQLException {

    }

    public void acceptFriendRequest(String userID1, String userID2) throws SQLException {
        String collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");

        // get users
        String query = "RETURN DOCUMENT(userId, " + userID2+ ")";
        User user1 = arangoDB.db(dbName).collection(collectionName).getDocument(userID1, User.class);
        User user2 = arangoDB.db(dbName).collection(collectionName).getDocument(userID2, User.class);

        UserInFriendsList userFL1 = new UserInFriendsList(user1);
        UserInFriendsList userFL2 = new UserInFriendsList(user2);

        // add user2 in user1's friendsList
        query = "FOR u IN " + collectionName + "\n\t FILTER u.userId == @id LET newFriends = PUSH(u.friendsList, @newFriend) UPDATE u WITH{ friends: newFriends } IN " + collectionName;
        Map<String, Object> bindVars = new HashMap<String, Object>();
        bindVars.put("id", userID1);
        bindVars.put("newFriend", userFL2);
        arangoDB.db(dbName).query(query, bindVars, null, User.class);

        // add user1 in user2's friendsList
        query = "FOR u IN " + collectionName + "\n\t FILTER u.userId == @id LET newFriends = PUSH(u.friendsList, @newFriend) UPDATE u WITH{ friends: newFriends } IN " + collectionName;
        bindVars = new HashMap<String, Object>();
        bindVars.put("id", userID2);
        bindVars.put("newFriend", userFL1);
        arangoDB.db(dbName).query(query, bindVars, null, User.class);


    }

    public void unfriendUser(String userID1, String userID2) throws SQLException {
        String collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");

        // get users
        User user1 = arangoDB.db(dbName).collection(collectionName).getDocument(userID1, User.class);
        User user2 = arangoDB.db(dbName).collection(collectionName).getDocument(userID2, User.class);

        UserInFriendsList userFL1 = new UserInFriendsList(user1);
        UserInFriendsList userFL2 = new UserInFriendsList(user2);

        // remove user2 from user1's friendsList
        String query = "FOR u IN " + collectionName + "\n\t FILTER u.userId == @id LET newFriends = REMOVE_VALUE(u.friendsList, @newFriend) UPDATE u WITH{ friends: newFriends } IN " + collectionName;
        Map<String, Object> bindVars = new HashMap<String, Object>();
        bindVars.put("id", userID1);
        bindVars.put("newFriend", userFL2);
        arangoDB.db(dbName).query(query, bindVars, null, User.class);

        // remove user1 from user2's friendsList
        query = "FOR u IN " + collectionName + "\n\t FILTER u.userId == @id LET newFriends = REMOVE_VALUE(u.friendsList, @newFriend) UPDATE u WITH{ friends: newFriends } IN " + collectionName;
        bindVars = new HashMap<String, Object>();
        bindVars.put("id", userID2);
        bindVars.put("newFriend", userFL1);
        arangoDB.db(dbName).query(query, bindVars, null, User.class);
    }

    public ArrayList<UserInFriendsList> getFriendsList(String userID){
        String collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");

        User user1 = arangoDB.db(dbName).collection(collectionName).getDocument(userID, User.class);
        return user1.getFriendsList();
    }
}

