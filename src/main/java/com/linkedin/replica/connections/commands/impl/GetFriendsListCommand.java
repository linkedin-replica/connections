package com.linkedin.replica.connections.commands.impl;

import java.sql.SQLException;
import java.util.HashMap;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.database.handlers.impl.ArangoFriendsListHandler;

/**
 *  Implementation of command design patterns for block user functionality
 */
public class GetFriendsListCommand extends Command {

    public GetFriendsListCommand(HashMap<String, Object> args){
        super(args);
    }

    @Override
    public Object execute() throws SQLException {
        validateArgs(new String[]{"userId"});
        String userId = (String) args.get("userId");
        ArangoFriendsListHandler dbHandler = (ArangoFriendsListHandler) this.dbHandler;
        return dbHandler.getFriendsList(userId);
    }

}