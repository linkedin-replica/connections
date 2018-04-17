package com.linkedin.replica.connections.database.handlers.impl;

import com.arangodb.ArangoDB;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.DatabaseConnection;
import com.linkedin.replica.connections.database.handlers.AddingRemovingFriends;
import com.linkedin.replica.connections.models.User;
import com.linkedin.replica.connections.models.UserInFriendsList;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AddingRemovingFriendsHandler extends AddingRemovingFriends {
    private Connection mySqlConnection;
    private ArangoDB arangoDB;
    private String dbName;
    public AddingRemovingFriendsHandler() throws SQLException, IOException, ClassNotFoundException {
        mySqlConnection = DatabaseConnection.getInstance().getMysqlConn();
        arangoDB = DatabaseConnection.getInstance().getArangodb();
        dbName = Configuration.getInstance().getArangoConfigProp("db.name");
    }

    public void acceptFriendRequest(String userID1, String userID2) throws SQLException {
        if(userID1.compareTo(userID2) > 0){
            String temp = userID1;
            userID1 = userID2;
            userID2 = temp;
        }

        String query = "{CALL accept_friend_request(?,?)}";
        CallableStatement stmt = mySqlConnection.prepareCall(query);
        stmt.setString(1, userID1);
        stmt.setString(2, userID2);
        stmt.executeQuery();

        String collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");

        // get users
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
        String query = "{CALL delete_user_friend_with_user(?,?)}";
        CallableStatement stmt = mySqlConnection.prepareCall(query);
        stmt.setString(1, userID1);
        stmt.setString(2, userID2);
        stmt.executeQuery();

        String collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");

        // get users
        User user1 = arangoDB.db(dbName).collection(collectionName).getDocument(userID1, User.class);
        User user2 = arangoDB.db(dbName).collection(collectionName).getDocument(userID2, User.class);

        UserInFriendsList userFL1 = new UserInFriendsList(user1);
        UserInFriendsList userFL2 = new UserInFriendsList(user2);

        // remove user2 from user1's friendsList
        query = "FOR u IN " + collectionName + "\n\t FILTER u.userId == @id LET newFriends = REMOVE_VALUE(u.friendsList, @newFriend) UPDATE u WITH{ friends: newFriends } IN " + collectionName;
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
}
