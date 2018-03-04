package main.java.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import main.java.commands.Command;
import main.java.config.Config;
import main.java.databaseHandlers.DatabaseHandler;


/**
 * Connections Service is responsible for taking input from controller, reading commands config file to 
 * get specific command responsible for handling input request and also get DatabaseHandler name
 * Associated with this command 
 * 
 * It will call command execute method after passing to its DatabaseHandler
 */
public class ConnectionsService {
	// load config file
	private Properties prop;
	private String commandsPackageName;
	private String dbHandlerPackageName;
	
	public ConnectionsService() throws FileNotFoundException, IOException{
		prop = new Properties();
		prop.load(new FileInputStream(Config.getInstance().getCommandConfigPath()));
		commandsPackageName = "main.java.commands.impl";
		dbHandlerPackageName = "main.java.databaseHandlers.impl";
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