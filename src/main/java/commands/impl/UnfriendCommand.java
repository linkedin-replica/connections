package main.java.commands.impl;

import java.sql.SQLException;

import main.java.commands.Command;

/**
 *  Implementation of command design patterns for unfriend user functionality
 */
public class UnfriendCommand extends Command{

	@Override
	public void execute() throws SQLException {
		String userID1 = args.get("userID1");
		String userID2 = args.get("userID2");
		dbHandler.unfriendUser(userID1, userID2);
	}

}
