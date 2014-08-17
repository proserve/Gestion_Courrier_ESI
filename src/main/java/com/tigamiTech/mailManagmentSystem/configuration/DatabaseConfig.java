package com.tigamiTech.mailManagmentSystem.configuration;

import java.util.Properties;

/**
 * Created by proserve on 04/06/14.
 */
public class DatabaseConfig {
    private String username="";
    private String password="";
    private String host="";
    private String port;
    private String databaseName ="";
    private static DatabaseConfig instance;
    private Properties properties = new Properties();

    public DatabaseConfig() {

    }

    public static DatabaseConfig getInstance(){
        if(instance == null) instance = new DatabaseConfig();
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        ConfigManager.getInstance().setConfiguration("username", username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        ConfigManager.getInstance().setConfiguration("password", password);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        ConfigManager.getInstance().setConfiguration("host", host);
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
        ConfigManager.getInstance().setConfiguration("port", String.valueOf(port));
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        ConfigManager.getInstance().setConfiguration("databaseName", databaseName);
    }



    public Properties getDatabaseConfigProperties(){
        try {
            properties.put("eclipselink.jdbc.url", "jdbc:postgresql://"+ConfigManager.getInstance().getConfiguration("host")+":"
                    +ConfigManager.getInstance().getConfiguration("port")+"/"+
                    ConfigManager.getInstance().getConfiguration("databaseName"));
            properties.put("eclipselink.jdbc.user", ConfigManager.getInstance().getConfiguration("username"));
            properties.put("eclipselink.jdbc.password", ConfigManager.getInstance().getConfiguration("password"));
            return properties;
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
          return null;
        }

    }
}
