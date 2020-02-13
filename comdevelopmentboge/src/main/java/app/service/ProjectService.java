package app.service;

import app.config.DbContext;
import app.db.*;
import app.gui.project.ProjectListFilter;
import org.apache.poi.hssf.record.DBCellRecord;

import javax.xml.transform.Result;
import java.awt.image.DataBuffer;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProjectService {

    private static ProjectService projectService = new ProjectService();
    public static ProjectService getProjectService(){ return  projectService;}

    public ArrayList<Project> getAllProjects(){
        ArrayList<Project> result = new ArrayList<>();

        String sql = "select p.*,customers.name  from projects p left join customers on customer_id=customers.id";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Project project = new Project();
                project.setId(rs.getInt(1));
                project.setProjectName(rs.getString(3));
                project.setProjectNumber(rs.getString(2));
                project.setPartNumber(rs.getString(4));
                project.setRos(rs.getString(5));
                project.setRoce(rs.getString(6));
                project.setVolumes(rs.getBigDecimal(7));
                project.setDdCost(rs.getBigDecimal(8));
                project.setPrototypeCost(rs.getBigDecimal(9));
                project.setLastUpdated(rs.getDate(10));
                project.setCustomerId(rs.getInt(11));
                project.setCustomerName(rs.getString(12));

                result.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
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

    public ArrayList<Project> findProjectsByCriteria(){

        ArrayList<Project> result = new ArrayList<>();

        String projectName = ProjectListFilter.getProjectListFilter().getProjectName();
        String projectNumber = ProjectListFilter.getProjectListFilter().getProjectNumber();
        String customer = ProjectListFilter.getProjectListFilter().getCustomer();

        String sql = "select p.* from projects p ";
        String criteria1 = "";
        String criteria2 = "";
        String criteria3 = "";

        boolean anyCriteria = false;

        //if any customer name typed, inner join with customers table
        if(customer != null && !"".equals(customer)){
            sql += " inner join customers on p.customer_id=customers.id ";
        }

        if(projectName != null && !"".equals(projectName)){
            anyCriteria = true;
            criteria1 = "projectName like '%"+projectName+"%'";
        }

        if(projectNumber != null && !"".equals(projectNumber)){
            anyCriteria = true;
            if(!criteria1.equals("")){
                criteria2 += "and projectNumber like '%" + projectNumber+"%'";
            } else{
                criteria2 += "projectNumber like'%" + projectNumber+"%'";
            }
        }

        if(customer != null && !"".equals(customer)){
            if (anyCriteria){
                criteria3 += " and customers.name like '%" + customer + "%'";
            } else{
                criteria3 += " customers.name like '%" + customer + "%'";
            }
        }

        sql+= ((!"".equals(criteria1) || !"".equals(criteria2) || !"".equals(criteria3) )?" where ":"") + criteria1 + criteria2 + criteria3 + ";";

        System.out.println(sql);
        try(Statement st = DbContext.getConnection().createStatement()){

            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Project project = new Project();
                project.setId(rs.getInt(1));
                project.setProjectName(rs.getString(3));
                project.setProjectNumber(rs.getString(2));
                project.setPartNumber(rs.getString(4));
                project.setRos(rs.getString(5));
                project.setRoce(rs.getString(6));
                project.setVolumes(rs.getBigDecimal(7));
                project.setDdCost(rs.getBigDecimal(8));
                project.setPrototypeCost(rs.getBigDecimal(9));
                project.setLastUpdated(rs.getDate(10));
                project.setCustomerId(rs.getInt(11));

                result.add(project);
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
        String sql = "select projects.*, customers.name  from projects left join customers on projects.customer_id=customers.id where projects.projectNumber=?";
        try(PreparedStatement st = DbContext.getConnection().prepareStatement(sql)){
            st.setString(1, projectNumber);

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                project.setId(rs.getInt(1));
                project.setProjectName(rs.getString(3));
                project.setProjectNumber(rs.getString(2));
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

    public Project findProjectByNum(String projectNumber){
        Project project = new Project();
        String sql = "select * from projects where projectNumber=?";
        try(PreparedStatement st = DbContext.getConnection().prepareStatement(sql)){
            st.setString(1, projectNumber);

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                project.setId(rs.getInt(1));
                project.setProjectName(rs.getString(3));
                project.setProjectNumber(rs.getString(2));
                project.setPartNumber(rs.getString(4));
                project.setRos(rs.getString(5));
                project.setRoce(rs.getString(6));
                project.setVolumes(rs.getBigDecimal(7));
                project.setDdCost(rs.getBigDecimal(8));
                project.setPrototypeCost(rs.getBigDecimal(9));
                project.setLastUpdated(rs.getDate(10));
                project.setCustomerId(rs.getInt(11));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return project;
    }

    public BigDecimal getPlanedDDCosts(String projektDef){
        BigDecimal plannedDDCosts = BigDecimal.ZERO;
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement("select DDCost from projects where projectNumber = ?")){
            preparedStatement.setString(1, projektDef);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                plannedDDCosts = rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plannedDDCosts;
    }

    public BigDecimal getPrototypeCosts(String projektDef){
        BigDecimal plannedPrototypeCosts = BigDecimal.ZERO;
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement("select prototypeCosts from projects where projectNumber = ?")){
            preparedStatement.setString(1, projektDef);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                plannedPrototypeCosts = rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plannedPrototypeCosts;
    }

    public Project findProjectById(Integer id){
        Project project = new Project();
        String sql = "select * from projects where id=?";
        try(PreparedStatement st = DbContext.getConnection().prepareStatement(sql)){
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                project.setId(rs.getInt(1));
                project.setProjectName(rs.getString(3));
                project.setProjectNumber(rs.getString(2));
                project.setPartNumber(rs.getString(4));
                project.setRos(rs.getString(5));
                project.setRoce(rs.getString(6));
                project.setVolumes(rs.getBigDecimal(7));
                project.setDdCost(rs.getBigDecimal(8));
                project.setPrototypeCost(rs.getBigDecimal(9));
                project.setLastUpdated(rs.getDate(10));
                project.setCustomerId(rs.getInt(11));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return project;
    }

    public ArrayList<String> getFreeProjectsNums()  {

        ArrayList<String> result = new ArrayList<>();

        String sql = "select projectNumber from projects as p left join administration as a on p.id = a.project_id where a.id is null";
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

    /**
     * import novych projektov zo SAP tabulky
     * @throws SQLException
     */
    public void importProjects() throws SQLException {
        String sqlInsert = "INSERT IGNORE INTO projects (projectNumber) " +
                "select distinct s.ProjektDef " +
                "from sap s";

        try(PreparedStatement s = DbContext.getConnection().prepareStatement(sqlInsert)){

            s.executeUpdate();


        }

    }
}
