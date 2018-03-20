package com.linkedin.replica.connections.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.handlers.DatabaseHandler;


/**
 * Connections Service is responsible for taking input from controller, reading com.linkedin.replica.connections.commands com.linkedin.replica.connections.config file to
 * get specific command responsible for handling input request and also get DatabaseHandler name
 * Associated with this command 
 * 
 * It will call command execute method after passing to its DatabaseHandler
 */
public class ConnectionsService {
	// load com.linkedin.replica.connections.config file
	private Properties prop;
	private String commandsPackageName;
	private String dbHandlerPackageName;
	
	public ConnectionsService() throws FileNotFoundException, IOException{
		prop = new Properties();
		prop.load(new FileInputStream(Configuration.getInstance().getCommandConfigPath()));
		commandsPackageName = "com.linkedin.replica.connections.commands.impl";
		dbHandlerPackageName = "com.linkedin.replica.connections.database.handlers.impl";
	}
		
	public  void serve(String commandName, HashMap<String, String> args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
			String commandClassName = commandsPackageName + "." + prop.getProperty(commandName + ".command");
			String handlerClassName = dbHandlerPackageName + "." + prop.getProperty(commandName+ ".dbHandler");
			
			// load class of type command and create an instance
			Class c = Class.forName(commandClassName);
			Object o = c.newInstance();
			Command command = (Command) o;
			
			// load class of type database handler
			c = Class.forName(handlerClassName);
			o = c.newInstance();
			DatabaseHandler dbHandler = (DatabaseHandler) o;
			
			// set args and dbHandler of command
			command.setArgs(args);
			command.setDbHandler(dbHandler);

			// execute command
			command.execute();
	}
}