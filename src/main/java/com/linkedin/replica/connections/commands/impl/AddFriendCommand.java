package com.linkedin.replica.connections.commands.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.messaging.SendNotificationHandler;
import com.linkedin.replica.connections.database.handlers.impl.ArangoMySQLFriendsHandler;

/**
 *  Implementation of command design patterns for add friend functionality
 */
public class AddFriendCommand extends Command {

	public AddFriendCommand(HashMap<String, Object> args){
		super(args);
	}

	@Override
	public Object execute() throws SQLException, IOException {
		validateArgs(new String[]{"userId"});
		validateArgs(new String[]{"userId1"});
		String userID1 = (String) args.get("userId");
		String userID2 = (String) args.get("userId1");
		ArangoMySQLFriendsHandler dbHandler = (ArangoMySQLFriendsHandler) this.dbHandler;
		dbHandler.addFriend(userID1, userID2);

		SendNotificationHandler.getInstance().sendNotification(userID2, "User wants to connect with you", "TODO link");

		return null;
	}

}
