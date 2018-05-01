package com.linkedin.replica.connections.commands.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.messaging.SendNotificationHandler;
import com.linkedin.replica.connections.database.handlers.impl.ArangoMySQLFriendsHandler;
import com.linkedin.replica.connections.services.Workers;

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

		Runnable sendNotificationRunnable = () -> {
			try {
				String myName = dbHandler.getUserName(userID1);
				String text = myName + " added you as a friend";
				String link = Configuration.getInstance().getAppConfigProp("route.friendRequests");
				SendNotificationHandler.getInstance().sendNotification(userID2, text, link);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		Workers.getInstance().submit(sendNotificationRunnable);

		return null;
	}

}
