package com.linkedin.replica.connections.commands.impl;

import java.sql.SQLException;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.database.handlers.impl.MySqlHandler;

/**
 *  Implementation of command design patterns for unblock user functionality
 */
public class UnblockUserCommand extends Command {

	@Override
	public Object execute() throws SQLException {
		String userID1 = (String) args.get("userID1");
		String userID2 = (String) args.get("userID2");
		MySqlHandler dbHandler = (MySqlHandler) this.dbHandler;
		dbHandler.unBlockUser(userID1, userID2);
		return null;
	}

}
