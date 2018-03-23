package com.linkedin.replica.connections.commands.impl;

import java.sql.SQLException;
import java.util.HashMap;

import com.linkedin.replica.connections.commands.Command;

/**
 *  Implementation of command design patterns for unfriend user functionality
 */
public class UnfriendCommand extends Command {

	public UnfriendCommand(HashMap<String, Object> args){
		super(args);
	}
	@Override
	public Object execute() throws SQLException {
		validateArgs(new String[]{"userID1"});
		validateArgs(new String[]{"userID2"});
		String userID1 = (String) args.get("userID1");
		String userID2 = (String) args.get("userID2");
		dbHandler.unfriendUser(userID1, userID2);
		return null;
	}

}
