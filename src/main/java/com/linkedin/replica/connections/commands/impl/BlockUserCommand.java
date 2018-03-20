package com.linkedin.replica.connections.commands.impl;

import java.sql.SQLException;

import com.linkedin.replica.connections.commands.Command;

/**
 *  Implementation of command design patterns for block user functionality
 */
public class BlockUserCommand extends Command {

	@Override
	public Object execute() throws SQLException {
		String userID1 = (String) args.get("userID1");
		String userID2 = (String) args.get("userID2");
		dbHandler.blockUser(userID1,userID2);
		return null;
	}
	
}
