package com.linkedin.replica.connections.commands.impl;

import java.sql.SQLException;

import com.linkedin.replica.connections.commands.Command;

/**
 *  Implementation of command design patterns for block user functionality
 */
public class BlockUserCommand extends Command {

	@Override
	public void execute() throws SQLException {
		String userID1 = args.get("userID1");
		String userID2 = args.get("userID2");
		dbHandler.blockUser(userID1,userID2);
	}
	
}
