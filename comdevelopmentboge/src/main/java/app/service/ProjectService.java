package app.service;

import app.config.DbContext;
import app.db.*;
import app.gui.project.ProjectListFilter;

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

        String sql = "select projectNumber from projects ";
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

    public Project findProjectByProjectName(String projectName){
        return null;
    }

    // sap-ProjektDef, projects-projectNumber jedinecny identifikator projektu
    public Project findProjectByProjectNumber(String projectNumber){
        Project project = new Project();
        String sql = "select projects.*, customers.name  from projects inner join customers on projects.customer_id=customers.id where projects.projectNumber=?";
        try(PreparedStatement st = DbContext.getConnection().prepareStatement(sql)){
            st.setString(1, projectNumber);

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                project.setId(rs.getInt(1));
                project.setProjectName(rs.getString(2));
                project.setProjectNumber(rs.getString(3));
                project.setPartNumber(rs.getString(4));
                project.setRos(rs.getString(5));
                project.setRoce(rs.getString(6));
                project.setVolumes(rs.getBigDecimal(7));
                project.setDdCost(rs.getBigDecimal(8));
                project.setPrototypeCost(rs.getBigDecimal(9));
                project.setLastUpdated(rs.getDate(10));
                project.setCustomerId(rs.getInt(11));
                project.setCustomerName(rs.getString(12));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return project;
    }

    public ArrayList<Project> findAllCustomerProjects(Customer customer){
        return null;
    }


}
