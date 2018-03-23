package com.linkedin.replica.connections.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.handlers.DatabaseHandler;


/**
 * Main Service is responsible for taking input from controller, reading com.linkedin.replica.connections.commands com.linkedin.replica.connections.config file to
 * get specific command responsible for handling input request and also get DatabaseHandler name
 * Associated with this command 
 * 
 * It will call command execute method after passing to its DatabaseHandler
 */
public class ConnectionsService {
	// load com.linkedin.replica.connections.config file
	private Configuration config;

	public ConnectionsService() throws FileNotFoundException, IOException{
		config = Configuration.getInstance();
	}
		
	public Object serve(String commandName, HashMap<String, Object> args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException {
		Class<?> commandClass = config.getCommandClass(commandName);
		Constructor constructor = commandClass.getConstructor(HashMap.class);
		Command command = (Command) constructor.newInstance(args);

		Class<?> dbHandlerClass = config.getHandlerClass(commandName);
		DatabaseHandler dbHandler = (DatabaseHandler) dbHandlerClass.newInstance();

		command.setDbHandler(dbHandler);

		return command.execute();
	}
}