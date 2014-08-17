package com.tigamiTech.mailManagmentSystem.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.io.File;

public class ConfigManager{

	private static ConfigManager instance;
	private XMLConfiguration xmlConfiguration;
	final File ConfigFile = new File("config.xml");

	private ConfigManager(){
		try {
                   
			xmlConfiguration = new XMLConfiguration(ConfigFile);
            setConfiguration("username", "alfresco");
            setConfiguration("password", "admin");
            setConfiguration("port", "5432");
            setConfiguration("host", "localhost");
            setConfiguration("databaseName", "alfresco");

            setConfiguration("alfrescoUsername", "admin");
            setConfiguration("alfrescoPassword", "admin");
            setConfiguration("alfrescoHost", "localhost");
            setConfiguration("alfrescoPort", "8080");
		} catch (ConfigurationException e) {
			System.out.println(e.getMessage());
		}
	}



    public static ConfigManager getInstance(){
		if(instance== null){
			instance = new ConfigManager();
		}
		return instance;
	}

	public String getConfiguration(String configurationName) {
		return xmlConfiguration.getString(configurationName);
	}

	public void setConfiguration(String key, String value) {

		xmlConfiguration.setProperty(key, value);
		try {
			xmlConfiguration.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
}
