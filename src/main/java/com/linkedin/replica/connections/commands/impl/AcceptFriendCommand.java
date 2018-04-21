package com.linkedin.replica.connections.commands.impl;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.database.handlers.impl.ArangoMySQLFriendsHandler;

import java.sql.SQLException;
import java.util.HashMap;

/**
 *  Implementation of command design patterns for accept friend functionality
 */
public class AcceptFriendCommand extends Command {

	public AcceptFriendCommand(HashMap<String, Object> args){
		super(args);
	}
	@Override
	public Object execute() throws SQLException {
		validateArgs(new String[]{"userId"});
		validateArgs(new String[]{"userId1"});
		String userID1 = (String) args.get("userId");
		String userID2 = (String) args.get("userId1");
		ArangoMySQLFriendsHandler dbHandler = (ArangoMySQLFriendsHandler) this.dbHandler;
		dbHandler.acceptFriendRequest(userID1, userID2);
		return null;
	}

}
