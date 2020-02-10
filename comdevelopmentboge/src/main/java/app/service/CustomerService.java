package app.service;

import app.config.DbContext;
import app.db.*;
import app.exception.DatabaseException;
import org.apache.poi.hssf.record.DBCellRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class CustomerService {

    private CustomerService(){}
    public static CustomerService getCustomerService(){return customerService;}
    private static CustomerService customerService = new CustomerService();

    public ArrayList<Customer> getAllCustomer(){
        ArrayList<Customer> result = new ArrayList<>();

        String sql = "select * from customers";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                result.add(new Customer(rs.getInt(1), rs.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Customer findCustomerById(Integer id){
        return null;
    }

    public Customer findCustomerByName(String name) {
        Customer customer = new Customer();
        String sql = "select id from customers where name=?";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){
            preparedStatement.setString(1, name);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                customer.setId(rs.getInt(1));
                customer.setName(name);
            } else{
                return null;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }


}
