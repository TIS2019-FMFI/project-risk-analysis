package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Registration {

    public void Register(){
        String url = "jdbc:mysql://localhost:3307/project_risk_analysis?serverTimezone=UTC";
        String username = "root";
        String password = "usbw";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Cant connect");
        }

        try(Connection connection = DriverManager.getConnection(url,username,password)){
            System.out.println("Connection succesful");


        } catch (SQLException e) {
            System.out.println("Connection failed or user does not exist");
        }
    }


    private boolean checkIfExists(){
        return false;
    }
}
