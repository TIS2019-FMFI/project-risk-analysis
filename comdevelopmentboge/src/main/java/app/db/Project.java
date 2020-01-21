package app.db;

import app.config.DbContext;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Project {
    private Integer id;
    private String projectNumber;
    private String projectName;
    private Integer customerId;
    private String customerName;
    private String partNumber;
    private String ros;
    private String roce;
    private BigDecimal volumes;
    private BigDecimal ddCost;
    private BigDecimal prototypeCost;
    private Date lastUpdated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getRos() {
        return ros;
    }

    public void setRos(String ros) {
        this.ros = ros;
    }

    public String getRoce() {
        return roce;
    }

    public void setRoce(String roce) {
        this.roce = roce;
    }

    public BigDecimal getVolumes() {
        return volumes;
    }

    public void setVolumes(BigDecimal volumes) {
        this.volumes = volumes;
    }

    public BigDecimal getDdCost() {
        return ddCost;
    }

    public void setDdCost(BigDecimal ddCost) {
        this.ddCost = ddCost;
    }

    public BigDecimal getPrototypeCost() {
        return prototypeCost;
    }

    public void setPrototypeCost(BigDecimal prototypeCost) {
        this.prototypeCost = prototypeCost;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void update(){
        String sql = "update projects set projectName=?, projectNumber=?, partNumber=?, ROS=?, ROCE=?, volumes=?, DDCost=?, prototypeCosts=?, " +
                "lastUpdate=?, customer_id =? where  id=?";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){
            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, projectNumber);
            preparedStatement.setString(3, partNumber);
            preparedStatement.setString(4, ros);
            preparedStatement.setString(5, roce);
            preparedStatement.setBigDecimal(6, volumes);
            preparedStatement.setBigDecimal(7, ddCost);
            preparedStatement.setBigDecimal(8, prototypeCost);
            if(lastUpdated!=null){
                preparedStatement.setDate(9, new java.sql.Date(lastUpdated.getTime()));
            } else{
                preparedStatement.setDate(9, null);
            }
            preparedStatement.setInt(10, customerId);
            preparedStatement.setInt(11, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}