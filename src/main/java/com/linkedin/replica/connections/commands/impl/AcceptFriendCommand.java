package com.linkedin.replica.connections.commands.impl;

import java.sql.SQLException;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.database.handlers.impl.AddingRemovingFriendsHandler;

/**
 *  Implementation of command design patterns for accept friend functionality
 */
public class AcceptFriendCommand extends Command {

	@Override
	public Object execute() throws SQLException {
		String userID1 = (String) args.get("userID1");
		String userID2 = (String) args.get("userID2");
		AddingRemovingFriendsHandler dbHandler = (AddingRemovingFriendsHandler) this.dbHandler;
		dbHandler.acceptFriendRequest(userID1, userID2);
		return null;
	}

}
