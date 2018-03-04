package main.java.commands.impl;

import java.sql.SQLException;

import main.java.commands.Command;

/**
 *  Implementation of command design patterns for add friend functionality
 */
public class AddFriendCommand extends Command{

	@Override
	public void execute() throws SQLException {
		String userID1 = args.get("userID1");
		String userID2 = args.get("userID2");
		dbHandler.addFriend(userID1, userID2);
	}

}
