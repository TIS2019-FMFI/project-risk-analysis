package app.config;

import app.gui.MyAlert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {

    private Properties properties;

    public PropertiesManager() {
        properties = new java.util.Properties();
        FileInputStream ip= null;
        try {
            ip = new FileInputStream("\\\\csrboge.corp\\skt\\TEAM\\CC9742710\\Individual\\BSTX-Internal\\2_Team\\PRA\\config.properties");
        } catch (FileNotFoundException e) {
            MyAlert.showError("Systém nenašiel súbor config.properties.\n" +
                    "Aplikácia bez neho nedokáže pokračovat a bude zatvorená.");
        }
        try {
            properties.load(ip);
        } catch (IOException e) {
            MyAlert.showError("Nepodarilo sa prečítať konfiguráciu zo súboru config.properties.");
        }
    }

    public String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }
}
