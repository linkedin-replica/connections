package com.linkedin.replica.connections.commands;

import java.sql.SQLException;
import java.util.HashMap;

import com.linkedin.replica.connections.database.handlers.DatabaseHandler;

/**
 * Command is an abstract class responsible for handling specific request and it communicates between
 * external input and internal functionality implementation
 */
public abstract class Command {
    protected HashMap<String, String> args;
    protected DatabaseHandler dbHandler;
    
    public Command(){}
    
    /**
     * Execute the command
     * @return The output (if any) of the command
     * 	LinkedHashMap preserve order of insertion so it will preserve this order when parsing to JSON
     * @throws SQLException 
     */
    public abstract void execute() throws SQLException;

	public void setArgs(HashMap<String, String> args) {
		this.args = args;
	}

	public void setDbHandler(DatabaseHandler dbHandler) {
		this.dbHandler = dbHandler;
	}
    
    
}