package app.service;

import app.config.DbContext;
import app.db.*;

import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectService {

    private static ProjectService projectService = new ProjectService();
    public static ProjectService getProjectService(){ return  projectService;}

    public Projects findProjectById(Integer id) throws ClassNotFoundException {
        Projects project = new Projects();
        String sql = "select projectNumber, projectName, partNumber, ROS, ROCE, volumes, D&DCosts, prototypeCosts, Customers.name from Projects cross join Customers on Projects.customerId=Customers.id where Projects.id=?";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){
            preparedStatement.setString(1,String.valueOf(id));

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                project.setId(id);
                project.setProjectNumber(rs.getString(2));
                project.setProjectName(rs.getString(3));
                project.setRos(rs.getString(4));
                project.setRoce(rs.getString(5));
                project.setVolumes(rs.getBigDecimal(6));
                project.setDdCost(rs.getBigDecimal(7));
                project.setPrototypeCost(rs.getBigDecimal(8));
                project.setCustomer(rs.getString(9));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project;
    }

    public ArrayList<String> getAllProjectNames() throws ClassNotFoundException {

        ArrayList<String> result = new ArrayList<>();

        String sql = "select Projects.projectName from Projects";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                result.add(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    public Projects findProjectByProjectName(String projectName){
        return null;
    }

    public Projects findProjectByProjectNumber(Integer projectNumber){
        return null;
    }

    public ArrayList<Projects> findAllCustomerProjects(Customers customer){
        return null;
    }


}
