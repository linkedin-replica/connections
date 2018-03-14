package commands.impl;

import java.sql.SQLException;

import commands.Command;

/**
 *  Implementation of command design patterns for unblock user functionality
 */
public class UnblockUserCommand extends Command {

	@Override
	public void execute() throws SQLException {
		String userID1 = args.get("userID1");
		String userID2 = args.get("userID2");
		dbHandler.unBlockUser(userID1, userID2);
	}

}
