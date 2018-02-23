package main.java.config;

public class Config {
	private String databaseConfigPath;
	private String commandConfigPath;
	private String arangoNamesConfigPath;
	private static Config instance;
 	
	private Config(String databaseConfigPath, String commandConfigPath,
			String arangoNamesConfigPath) {
		this.databaseConfigPath = databaseConfigPath;
		this.commandConfigPath = commandConfigPath;
		this.arangoNamesConfigPath = arangoNamesConfigPath;
	}
	
	public static Config getInstance(String databaseConfigPath, String commandConfigPath,
			String arangoNamesConfigPath) {
		
		if(instance == null){
			synchronized (Config.class) {
				if(instance == null){
					instance = new Config(databaseConfigPath, commandConfigPath, arangoNamesConfigPath);
				}
			}
		}	
		return instance;
	}

	public static Config getInstance(){
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
