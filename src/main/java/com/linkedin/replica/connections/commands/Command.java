package com.linkedin.replica.connections.commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import com.linkedin.replica.connections.database.handlers.DatabaseHandler;
import com.linkedin.replica.connections.exceptions.ConnectionsException;

/**
 * Command is an abstract class responsible for handling specific request and it communicates between
 * external input and internal functionality implementation
 */
public abstract class Command {
    protected HashMap<String, Object> args;
    protected DatabaseHandler dbHandler;
    
    public Command(HashMap<String, Object> args){
    	this.args = args;
	}
    
    /**
     * Execute the command
     * @return The output (if any) of the command
     * 	LinkedHashMap preserve order of insertion so it will preserve this order when parsing to JSON
     * @throws SQLException 
     */
    public abstract Object execute() throws SQLException, NoSuchMethodException, IllegalAccessException, IOException;

	public void setDbHandler(DatabaseHandler dbHandler) {
		this.dbHandler = dbHandler;
	}

    protected void validateArgs(String[] requiredArgs) {
        for(String arg: requiredArgs)
            if(!args.containsKey(arg)) {
                String exceptionMsg = String.format("Cannot execute command. %s argument is missing", arg);
                throw new ConnectionsException(exceptionMsg);
            }
    }
    
    
}