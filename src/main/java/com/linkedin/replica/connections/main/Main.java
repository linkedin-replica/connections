package com.linkedin.replica.connections.main;

import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.controller.Server;
import com.linkedin.replica.connections.database.DatabaseConnection;
import com.linkedin.replica.connections.messaging.MessageReceiver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;



public class Main {
	
	public static void start(String... args) throws FileNotFoundException, ClassNotFoundException, IOException, SQLException, InterruptedException {
		if(args.length != 4)
			throw new IllegalArgumentException("Expected three arguments. 1-database.config file path "
					+ "2- commands.config file path  3- arango_name file path 4- controller.config file path");
		
		// create singleton instance of Configuration class that will hold configuration files paths
		Configuration.init(args[0], args[1], args[1], args[2], args[3]);
		
		// create singleton instance of DatabaseConnection class that is responsible for intiating connections
		// with databases
		DatabaseConnection.getInstance();

		// start tasks
		Runnable clientMessageRunnable = () -> {
			try {
				new MessageReceiver();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO log
			}
		};

		startTask(clientMessageRunnable, "Client Message Receiver");

		new Server("localhost", 8000).start();
	}

	private static void startTask(Runnable runnable, String name) {
		Thread thread = new Thread(runnable);
		System.out.println("Starting thread " + thread.getId() + " for " + name);
		thread.start();

	}
	
	public static void shutdown() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException{
		DatabaseConnection.getInstance().closeConnections();
	}
	
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException, SQLException, InterruptedException {
		Main.start(args);
	}
}
