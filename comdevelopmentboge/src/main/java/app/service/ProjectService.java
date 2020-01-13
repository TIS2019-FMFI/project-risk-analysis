package app.service;

import app.config.DbContext;
import app.db.*;
import app.gui.project.ProjectListFilter;

import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProjectService {

    private static ProjectService projectService = new ProjectService();
    public static ProjectService getProjectService(){ return  projectService;}

    public Projects findProjectById(Integer id) {
        Projects project = new Projects();
        String sql = "select projectNumber, projectName, partNumber, ROS, ROCE, volumes, DDCost, prototypeCosts, Customers.name from projects cross join Customers on Projects.customer_id=Customers.id where projects.id=?";
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

    public ArrayList<String> getAllProjectNames()  {

        ArrayList<String> result = new ArrayList<>();

        String sql = "select projectName from projects";
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

    public ArrayList<String> getAllProjectNumbers() {
        ArrayList<String> result = new ArrayList<>();

        String sql = "select projectNumber from projects";
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

    public ArrayList<String> findProjectsByCriteria(){

        ArrayList<String> result = new ArrayList<>();

        String projectName = ProjectListFilter.getProjectListFilter().getProjectName();
        String projectNumber = ProjectListFilter.getProjectListFilter().getProjectNumber();
        int customer = ProjectListFilter.getProjectListFilter().getCustomer();

        String sql = "select projectName from projects ";
        String criteria1 = "";
        String criteria2 = "";
        String criteria3 = "";
        if(projectName != null){
            criteria1 = "projectName='"+projectName+"'";
        }

        if(projectNumber != null){
            if(!criteria1.equals("")){
                criteria2 += "and projectNumber='" + projectNumber+"'";
            } else{
                criteria2 += "projectNumber='" + projectNumber+"'";
            }
        }

        if(customer != 0){
            criteria3 = " inner join customers on customers.id="+customer;
        }
        sql+= criteria3 + ((!criteria1.equals("") || !criteria2.equals("") )?" where ":"") + criteria1 + criteria2  + ";";

        System.out.println(sql);
        try(Statement st = DbContext.getConnection().createStatement()){

            ResultSet rs = st.executeQuery(sql);
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

    public ArrayList<Projects> findAllCustomerProjects(Customer customer){
        return null;
    }


}
