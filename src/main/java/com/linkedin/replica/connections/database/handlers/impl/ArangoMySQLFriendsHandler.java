package com.linkedin.replica.connections.database.handlers.impl;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.DatabaseConnection;
import com.linkedin.replica.connections.database.handlers.FriendsHandler;
import com.linkedin.replica.connections.models.User;
import com.linkedin.replica.connections.models.UserInFriendsList;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
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

    /**
     * Add user a friend
     * User with userID1 adds user with userID2 as friend
     *
     * @param userId
     */
    @Override
    public UserInFriendsList[] getFriendRequests(String userId) throws SQLException {

        PreparedStatement ps = mySqlConnection.prepareStatement("SELECT * FROM user_friends_with_user\n" +
                "WHERE (user1_id = ? AND is_accepted = 1) OR (user2_id = ? AND is_accepted = 0)");

        ps.setString(1, userId);
        ps.setString(2, userId);
        ResultSet res = ps.executeQuery();

        ArrayList<String> ids = new ArrayList<>();
        int size = 0;
        while(res.next()){
            size++;
            if(res.getString(1).equals(userId))
                ids.add(res.getString(2));
            else
                ids.add(res.getString(1));
        }
        String[] ids1 = new String[ids.size()];
        for (int i = 0; i < ids1.length; i++) {
            ids1[i] = ids.get(i);
        }
        String collectionName = Configuration.getInstance().getArangoConfigProp("collection.users.name");
        String query = "For u IN " + collectionName + " FILTER u.userId in @ids return {userId: u.userId, firstName : u.firstName, lastName : u.lastName, profilePictureUrl : u.profilePictureUrl}";
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("ids", ids1);
        ArangoCursor<UserInFriendsList> cursor = arangoDB.db(dbName).query(query, bindVars, null, UserInFriendsList.class);
        ArrayList<UserInFriendsList> users = new ArrayList<>();
        while(cursor.hasNext())
            users.add(cursor.next());

        return users.toArray(new UserInFriendsList[0]);
    }

    public void addFriend(String userID1, String userID2) throws SQLException {
        int status = -1;
        if(userID1.compareTo(userID2) < 1)
            status = 0; // user 1 adds user 2;
        else{
            String temp = userID1;
            userID1 = userID2;
            userID2 = temp;
            status = 1; // user 2 adds user 1;
        }

        String query = "{CALL Add_Friend(?,?,?)}";
        CallableStatement stmt = mySqlConnection.prepareCall(query);
        stmt.setString(1, userID1);
        stmt.setString(2, userID2);
        stmt.setInt(3, status);
        stmt.executeQuery();
    }
}
