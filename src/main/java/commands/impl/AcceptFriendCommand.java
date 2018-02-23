package main.java.commands.impl;

import java.sql.SQLException;

import main.java.commands.Command;

/**
 *  Implementation of command design patterns for block user functionality
 */
public class AcceptFriendCommand extends Command{

	@Override
	public void execute() throws SQLException {
		String userID1 = args.get("userID1");
		String userID2 = args.get("userID2");
		dbHandler.acceptFriendRequest(userID1, userID2);
	}

}
