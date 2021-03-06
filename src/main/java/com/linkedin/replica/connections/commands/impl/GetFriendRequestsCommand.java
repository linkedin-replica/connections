package com.linkedin.replica.connections.commands.impl;

import java.sql.SQLException;
import java.util.HashMap;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.database.handlers.impl.ArangoFriendsListHandler;
import com.linkedin.replica.connections.database.handlers.impl.ArangoMySQLFriendsHandler;

/**
 *  Implementation of command design patterns for block user functionality
 */
public class GetFriendRequestsCommand extends Command {

    public GetFriendRequestsCommand(HashMap<String, Object> args){
        super(args);
    }

    @Override
    public Object execute() throws SQLException {
        validateArgs(new String[]{"userId"});
        String userId = (String) args.get("userId");
        ArangoMySQLFriendsHandler dbHandler = (ArangoMySQLFriendsHandler) this.dbHandler;
        return dbHandler.getFriendRequests(userId);
    }

}