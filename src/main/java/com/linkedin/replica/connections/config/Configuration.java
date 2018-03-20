package com.linkedin.replica.connections.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.database.handlers.DatabaseHandler;


public class Configuration {
	private final Properties appConfig = new Properties();
	private final Properties commandConfig = new Properties();
	private final Properties databaseConfig = new Properties();
//	private final Properties arangoConfig = new Properties();
	private final Properties controllerConfig = new Properties();

	private String appConfigPath;
	private String databaseConfigPath;
//	private String arangoConfigPath;
	private String commandsConfigPath;
	private String controllerConfigPath;

	private boolean isAppConfigModified;
	private boolean isArangoConfigModified;
	private boolean isCommandsConfigModified;
//    private boolean isControllerConfigModified;

	private static Configuration instance;

	private Configuration(String appConfigPath, String databaseConfigPath, /*String arangoConfigPath,*/ String commandsConfigPath, String controllerConfigPath) throws IOException {
		populateWithConfig(appConfigPath, appConfig);
		populateWithConfig(databaseConfigPath, databaseConfig);
//		populateWithConfig(arangoConfigPath, arangoConfig);
		populateWithConfig(commandsConfigPath, commandConfig);
		populateWithConfig(controllerConfigPath, controllerConfig);

		this.appConfigPath = appConfigPath;
		this.databaseConfigPath = databaseConfigPath;
//		this.arangoConfigPath = arangoConfigPath;
		this.commandsConfigPath = commandsConfigPath;
		this.controllerConfigPath = controllerConfigPath;
	}

	public static Configuration getInstance() {
		return instance;
	}

	public static void init(String appConfigPath, String databaseConfigPath, /*String arangoConfigPath,*/ String commandsConfigPath, String controllerConfigPath) throws IOException {
		instance = new Configuration(appConfigPath, databaseConfigPath, /*arangoConfigPath,*/ commandsConfigPath, controllerConfigPath);
	}

	private static void populateWithConfig(String configFilePath, Properties properties) throws IOException {
		FileInputStream inputStream = new FileInputStream(configFilePath);
		properties.load(inputStream);
		inputStream.close();
	}

	public Class getCommandClass(String commandName) throws ClassNotFoundException {
		String commandsPackageName = Command.class.getPackage().getName() + ".impl";
		String commandClassPath = commandsPackageName + '.' + commandConfig.get(commandName);
		return Class.forName(commandClassPath);
	}

	public Class getHandlerClass(String commandName) throws ClassNotFoundException {
		String handlerPackageName = DatabaseHandler.class.getPackage().getName() + ".impl";
		String handlerClassPath = handlerPackageName + "." + commandConfig.get(commandName + ".handler");
		return Class.forName(handlerClassPath);
	}

	public String getDatabaseConfigProp(String key) {
		return databaseConfig.getProperty(key);
	}

	public String getDatabaseConfigPath(){
		return databaseConfigPath;
	}

//	public String getArangoConfigProp(String key) {
//		return arangoConfig.getProperty(key);
//	}

	public String getControllerConfigProp(String key){
		return controllerConfig.getProperty(key);
	}

	public String getCommandConfigProp(String key){
		return commandConfig.getProperty(key);
	}

	public String getCommandsConfigPath(){
		return commandsConfigPath;
	}

	public String getAppConfigProp(String key){
		return appConfig.getProperty(key);
	}

	public void setAppControllerProp(String key, String val){
		if(val != null)
			databaseConfig.setProperty(key, val);
		else
			databaseConfig.remove(key); // remove property if val is null

		isAppConfigModified = true;
	}

//	public void setArrangoConfigProp(String key, String val){
//		if(val != null)
//			arangoConfig.setProperty(key, val);
//		else
//			arangoConfig.remove(key); // remove property if val is null
//
//		isArangoConfigModified = true;
//	}

	public void setCommandsConfigProp(String key, String val){
		if(val != null)
			commandConfig.setProperty(key, val);
		else
			commandConfig.remove(key); // remove property if val is null

		isCommandsConfigModified = true;
	}

	/**
	 * Commit changes to write modifications in configuration files
	 * @throws IOException
	 */
	public void commit() throws IOException{
		if(isAppConfigModified){
			writeConfig(databaseConfigPath, databaseConfig);
			isAppConfigModified = false;
		}

//		if(isArangoConfigModified){
//			writeConfig(arangoConfigPath, arangoConfig);
//			isArangoConfigModified = false;
//		}

		if(isCommandsConfigModified){
			writeConfig(commandsConfigPath, commandConfig);
			isCommandsConfigModified = false;
		}
	}

	private void writeConfig(String filePath, Properties properties) throws IOException{
		// delete configuration file and then re-write it
		Files.deleteIfExists(Paths.get(filePath));
		OutputStream out = new FileOutputStream(filePath);
		properties.store(out, "");
		out.close();
	}
}