package main.java.commands.impl;

import main.java.commands.Command;

/**
 *  Implementation of command design patterns for block user functionality
 */
public class UnfriendCommand extends Command{

	@Override
	public void execute() {
		String userID1 = args.get("userID1");
		String userID2 = args.get("userID2");
		dbHandler.unfriendUser(userID1, userID2);
	}

}
