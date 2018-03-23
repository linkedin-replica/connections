package com.linkedin.replica.connections.commands.impl;

import java.sql.SQLException;
import java.util.HashMap;

import com.linkedin.replica.connections.commands.Command;

/**
 *  Implementation of command design patterns for add friend functionality
 */
public class AddFriendCommand extends Command {

	public AddFriendCommand(HashMap<String, Object> args){
		super(args);
	}

	@Override
	public Object execute() throws SQLException {
		String userID1 = (String) args.get("userID1");
		String userID2 = (String) args.get("userID2");
		dbHandler.addFriend(userID1, userID2);
		return null;
	}

}
