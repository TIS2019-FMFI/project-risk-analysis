package app.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {

    private Properties properties;

    public PropertiesManager() throws IOException {
        properties = new java.util.Properties();
        FileInputStream ip= new FileInputStream("C:/project-risk-analysis/config.properties");
        properties.load(ip);
    }

    public String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }
}
