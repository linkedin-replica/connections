package com.linkedin.replica.connections.commands.impl;

import java.sql.SQLException;
import java.util.HashMap;

import com.linkedin.replica.connections.commands.Command;

/**
 *  Implementation of command design patterns for unblock user functionality
 */
public class UnblockUserCommand extends Command {

	public UnblockUserCommand(HashMap<String, Object> args){
		super(args);
	}

	@Override
	public Object execute() throws SQLException {
		validateArgs(new String[]{"userID1"});
		validateArgs(new String[]{"userID2"});
		String userID1 = (String) args.get("userID1");
		String userID2 = (String) args.get("userID2");
		dbHandler.unBlockUser(userID1, userID2);
		return null;
	}

}
