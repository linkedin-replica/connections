package main.java.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import main.java.config.Config;


public class Main {
	
	public static void start(String... args) throws FileNotFoundException, ClassNotFoundException, IOException, SQLException{
		if(args.length != 3)
			throw new IllegalArgumentException("Expected three arguments. 1-database_config file path "
					+ "2- command_config file path  3- arango_name file path");
		
		// create singleton instance of Configuration class that will hold configuration files paths
		Config.getInstance(args[0], args[1], args[2]);
		
		// create singleton instance of DatabaseConnection class that is responsible for intiating connections
		// with databases
		DatabaseConnection.getInstance();
	}
	
	public static void shutdown() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException{
		DatabaseConnection.getInstance().closeConnections();
	}
	
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		Main.start(args);
	}
}
