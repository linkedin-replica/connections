package com.linkedin.replica.connections.database;

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
	
	private DatabaseConnection() throws FileNotFoundException, IOException, SQLException, ClassNotFoundException{
		properties = new Properties();
		properties.load(new FileInputStream(Configuration.getInstance().getDatabaseConfigPath()));
		mysqlConn = getNewMysqlDB();

//		redis = new Jedis();
	}
	
	/**
	 * To reduce use of synchronization, use  double-checked locking.
	 * we first check to see if an instance is created, and if not, then we synchronize. This
	 * way, we only synchronize the first time which is the initialization phase.
	 * 
	 * @return
	 * 		DatabaseConnection singleton instance
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 */
	public static DatabaseConnection getInstance() throws FileNotFoundException, IOException, SQLException, ClassNotFoundException{
		if(instance == null){
			synchronized (DatabaseConnection.class) {
				if(instance == null){
					instance = new DatabaseConnection();
				}
			}
		}	
		return instance;
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