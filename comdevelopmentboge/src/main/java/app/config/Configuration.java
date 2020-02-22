package app.config;

import app.App;
import app.gui.MyAlert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * trieda je zodpovedna za vytvorenie spojenia s datbazou
 */
public class Configuration {
    private static  String url;
    private static  String username;
    private static String password;

    /**
     * inicializuje pripojenie do databazy podla udajov z config.properties suboru
     */
    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            MyAlert.showError("Došlo k chybe počas získavania databázového spojenia, kontaktujte správcu.");
        }

        try {
            configureDatabaseConnection();
        } catch (IOException e) {
            MyAlert.showError("Došlo k chybe pri konfigurovaní databázového" +
                    "pripojenia. Uistite sa, že ste nastavili potrebné informácie v " +
                    "súbore C:/project-risk-analysis/config.properties");
        }

        try{
            DbContext.setConnection(DriverManager.getConnection(url,username,password));
        } catch (SQLException e) {
            MyAlert.showError("Došlo k chybe počas získavania databázového spojenia, kontaktujte správcu.");
        }
    }

    /**
     * ziska udaje z konfiguracneho suboru config.properties, podla ktorych sa aplikacia pripoji do databazy
     * @throws IOException
     */
    private static void configureDatabaseConnection() throws IOException {

        url = App.getPropertiesManager().getProperty("url");
        username = App.getPropertiesManager().getProperty("database.username");
        password = App.getPropertiesManager().getProperty("database.password");

    }
}
