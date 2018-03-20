package com.linkedin.replica.connections.config;

public class Configuration {
	private String databaseConfigPath;
	private String commandConfigPath;
	private String arangoNamesConfigPath;
	private static Configuration instance;
 	
	private Configuration(String databaseConfigPath, String commandConfigPath,
						  String arangoNamesConfigPath) {
		this.databaseConfigPath = databaseConfigPath;
		this.commandConfigPath = commandConfigPath;
		this.arangoNamesConfigPath = arangoNamesConfigPath;
	}
	
	public static Configuration getInstance(String databaseConfigPath, String commandConfigPath,
											String arangoNamesConfigPath) {
		
		if(instance == null){
			synchronized (Configuration.class) {
				if(instance == null){
					instance = new Configuration(databaseConfigPath, commandConfigPath, arangoNamesConfigPath);
				}
			}
		}	
		return instance;
	}

	public static Configuration getInstance(){
		return instance;
	}
	
	public String getDatabaseConfigPath() {
		return databaseConfigPath;
	}

	public String getCommandConfigPath() {
		return commandConfigPath;
	}

	public String getArangoNamesConfigPath() {
		return arangoNamesConfigPath;
	}
}
