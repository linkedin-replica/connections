package com.linkedin.replica.connections.commands.impl;

import java.sql.SQLException;
import java.util.HashMap;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.database.handlers.impl.MySqlHandler;

/**
 *  Implementation of command design patterns for unblock user functionality
 */
public class UnblockUserCommand extends Command {

	public UnblockUserCommand(HashMap<String, Object> args){
		super(args);
	}

	@Override
	public Object execute() throws SQLException {
		validateArgs(new String[]{"userId"});
		validateArgs(new String[]{"userId1"});
		String userID1 = (String) args.get("userId");
		String userID2 = (String) args.get("userId1");
		MySqlHandler dbHandler = (MySqlHandler) this.dbHandler;
		dbHandler.unBlockUser(userID1, userID2);
		return null;
	}

}
