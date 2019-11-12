package GUI;

import DbConnect.DbContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Login {
    public void login(){
        String url = "jdbc:mysql://localhost:3307/project_risk_analysis?serverTimezone=UTC";
        String username = "test";
        String password = "test";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Cant connect");
        }

        try(Connection connection = DriverManager.getConnection(url,username,password)){
            DbContext.setConnection(connection);
            System.out.println("Connection succesful");
        } catch (SQLException e) {
            System.out.println("Connection failed or user does not exist");
        }
    }
}
