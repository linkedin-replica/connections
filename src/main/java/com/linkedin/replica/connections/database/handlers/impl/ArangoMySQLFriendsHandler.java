package com.linkedin.replica.connections.database.handlers.impl;

import com.arangodb.ArangoDB;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.DatabaseConnection;
import com.linkedin.replica.connections.database.handlers.FriendsHandler;
import com.linkedin.replica.connections.models.User;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ArangoMySQLFriendsHandler implements FriendsHandler {
    private Connection mySqlConnection;
    private ArangoDB arangoDB;
    private String dbName;
    public ArangoMySQLFriendsHandler() throws SQLException, IOException, ClassNotFoundException {
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

        // add user2 in user1's friendsList
        query = "FOR u IN " + collectionName + " FILTER u.userId == @id " +
                "LET newFriends = PUSH(u.friendsList, @newFriend) " +
                "UPDATE u WITH{ friendsList : newFriends } IN " + collectionName;
        Map<String, Object> bindVars = new HashMap<String, Object>();
        bindVars.put("id", userID1);
        bindVars.put("newFriend", userID2);
        arangoDB.db(dbName).query(query, bindVars, null, null);

        // add user1 in user2's friendsList
        query = "FOR u IN " + collectionName + " FILTER u.userId == @id " +
                "LET newFriends = PUSH(u.friendsList, @newFriend) " +
                "UPDATE u WITH{ friendsList : newFriends } IN " + collectionName;
        bindVars = new HashMap<String, Object>();
        bindVars.put("id", userID2);
        bindVars.put("newFriend", userID1);
        arangoDB.db(dbName).query(query, bindVars, null, null);
    }

    public void unfriendUser(String userID1, String userID2) throws SQLException {
        String query = "{CALL delete_user_friend_with_user(?,?)}";
        CallableStatement stmt = mySqlConnection.prepareCall(query);
        stmt.setString(1, userID1);
        stmt.setString(2, userID2);
        stmt.executeQuery();

        String collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");

        // remove user2 from user1's friendsList
        query = "FOR u IN " + collectionName + "\n\t FILTER u.userId == @id " +
                "LET newFriends = REMOVE_VALUE(u.friendsList, @newFriend) " +
                "UPDATE u WITH{ friendsList : newFriends } IN " + collectionName;
        Map<String, Object> bindVars = new HashMap<String, Object>();
        bindVars.put("id", userID1);
        bindVars.put("newFriend", userID2);
        arangoDB.db(dbName).query(query, bindVars, null, User.class);

        // remove user1 from user2's friendsList
        query = "FOR u IN " + collectionName + "\n\t FILTER u.userId == @id " +
                "LET newFriends = REMOVE_VALUE(u.friendsList, @newFriend) " +
                "UPDATE u WITH{ friendsList : newFriends } IN " + collectionName;
        bindVars = new HashMap<String, Object>();
        bindVars.put("id", userID2);
        bindVars.put("newFriend", userID1);
        arangoDB.db(dbName).query(query, bindVars, null, User.class);
    }

    public void addFriend(String userID1, String userID2) throws SQLException {
        int status = -1;
        if (userID1.compareTo(userID2) < 1)
            status = 0; // user 1 adds user 2;
        else {
            String temp = userID1;
            userID1 = userID2;
            userID2 = temp;
            status = 1; // user 2 adds user 1;
        }

        String query = "{CALL Insert_Added_Friend(?,?,?)}";
        CallableStatement stmt = mySqlConnection.prepareCall(query);
        stmt.setString(1, userID1);
        stmt.setString(2, userID2);
        stmt.setInt(3, status);
        stmt.executeQuery();
    }


    public String getUserName(String userId) {
        String collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");
        String query = "let u = (DOCUMENT(@id)) return concat(u.firstName, ' ', u.lastName)";
        Map<String, Object> bindVars = new HashMap<>();
        bindVars.put("id", collectionName + "/" + userId);
        return arangoDB.db(dbName).query(query, bindVars, null, String.class).next();
    }
}
