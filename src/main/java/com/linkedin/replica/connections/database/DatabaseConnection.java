package com.linkedin.replica.connections.database;

import com.arangodb.ArangoDB;
import com.linkedin.replica.connections.config.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//import redis.clients.jedis.Jedis;

//import com.arangodb.ArangoDB;

/**
 *  DatabaseConnection is a singleton class responsible for reading database com.linkedin.replica.connections.config file and initiate
 *  connections to databases
 */
public class DatabaseConnection {
	private Connection mysqlConn;
//	private Jedis redis;
	
	private static DatabaseConnection instance;
	private Properties properties;
	private Configuration config;
	private ArangoDB arangodb;


	private DatabaseConnection() throws IOException, SQLException, ClassNotFoundException{
		properties = new Properties();
		config = Configuration.getInstance();
		properties.load(new FileInputStream(Configuration.getInstance().getDatabaseConfigPath()));
		mysqlConn = getNewMysqlDB();
		initializeArangoDB();
	}

	public static void init() throws SQLException, IOException, ClassNotFoundException {
		instance = new DatabaseConnection();
	}

	public static DatabaseConnection getInstance() {
		return instance;
	}

	public ArangoDB getArangodb() {
		return arangodb;
	}

	/**
	 * Instantiate ArangoDB
	 * @return
	 */
	private void initializeArangoDB() {
		arangodb = new ArangoDB.Builder()
				.user(config.getArangoConfigProp("arangodb.user"))
				.password(config.getArangoConfigProp("arangodb.password"))
				.build();
	}

	/**
	 * Implement the clone() method and throw an exception so that the singleton cannot be cloned.
	 */
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException("DatabaseConnection singleton, cannot be clonned");
	}

	/**
	 * Instantiate Mysql
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private Connection getNewMysqlDB() throws SQLException, ClassNotFoundException{
		// This will load the MySQL driver, each DB has its own driver
		Class.forName(properties.getProperty("mysql.database-driver"));
		// create new connection and return it
		return DriverManager.getConnection(properties.getProperty("mysql.url"),
				properties.getProperty("mysql.userName"),
				properties.getProperty("mysql.password"));
	}
	
	public void closeConnections() throws SQLException{
		
		if(mysqlConn != null)
			mysqlConn.close();
	}

	public Connection getMysqlConn() {
		return mysqlConn;
	}
	
}