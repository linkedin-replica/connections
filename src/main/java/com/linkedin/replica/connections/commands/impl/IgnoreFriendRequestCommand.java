package com.linkedin.replica.connections.commands.impl;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.database.handlers.BlockingHandler;
import com.linkedin.replica.connections.database.handlers.impl.MySQLBlockingHandler;

import java.sql.SQLException;
import java.util.HashMap;

public class IgnoreFriendRequestCommand extends Command {

    public IgnoreFriendRequestCommand(HashMap<String, Object> args) {
        super(args);
    }

    /**
     * Execute the command
     *
     * @return The output (if any) of the command
     * LinkedHashMap preserve order of insertion so it will preserve this order when parsing to JSON
     * @throws SQLException
     */
    @Override
    public Object execute() throws SQLException, NoSuchMethodException, IllegalAccessException {
        validateArgs(new String[]{"userId"});
        validateArgs(new String[]{"userId1"});
        String userID1 = (String) args.get("userId");
        String userID2 = (String) args.get("userId1");
        ((BlockingHandler)dbHandler).ignoreRequest(userID1, userID2);
        return null;
    }
}
