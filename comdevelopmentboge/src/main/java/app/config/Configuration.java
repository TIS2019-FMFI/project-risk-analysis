package app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Configuration {
    private static  String url = "jdbc:mysql://localhost:3307/project_risk_analysis?serverTimezone=UTC";
    private static  String username = "root";
    private static String password = "usbw";

    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Cant connect");
        }
        try{
            DbContext.setConnection(DriverManager.getConnection(url,username,password));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
