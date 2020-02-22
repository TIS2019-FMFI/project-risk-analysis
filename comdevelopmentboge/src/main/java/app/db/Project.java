package app.db;

import app.config.DbContext;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project extends Crud<Project> {
    /**
     * Objekt typu projekt
     */

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

    /**
     * Ziskanie a nastavenie parametrov objektu typu Project
     */


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

    /**
     * Ziskanie atributov objektu typu Project
     * @return
     */
    //for purpose of creating pdf tables
    //returns array of all attributes of a project in given order
    public List<String> getAllAttributesValues(){
        List<String>  attributes = new ArrayList<>();
        attributes.addAll(List.of((projectNumber!=null)?projectNumber:"", (customerName!=null)?customerName:"", (projectName!=null)?projectName:"",
                (partNumber!=null)?partNumber:"", (ros!=null)?ros:"", (roce!=null)?roce:"", (volumes!=null)?String.valueOf(volumes):"",
                (ddCost!=null)?String.valueOf(ddCost):"", (ddCost!=null)?String.valueOf(prototypeCost):""));
        return attributes;
    }

    /**
     * Vymaze projekt z tabulky
     */
    public void update()  {
        try {
            String sql = "update projects set projectName=?, projectNumber=?, partNumber=?, ROS=?, ROCE=?, volumes=?, DDCost=?, prototypeCosts=?, " +
                    "lastUpdate=?, customer_id =? where  id=?";
            update(DbContext.getConnection().prepareStatement(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * Doplni udaje do SQL dopytu pre vymazanie/aktualizovanie projektu
     * @param s SQL dopyt
     * @return
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        s.setString(1, projectName);
        s.setString(2, projectNumber);
        s.setString(3, partNumber);
        s.setString(4, ros);
        s.setString(5, roce);
        s.setBigDecimal(6, volumes);
        s.setBigDecimal(7, ddCost);
        s.setBigDecimal(8, prototypeCost);
        if(lastUpdated!=null){
            s.setDate(9, new java.sql.Date(lastUpdated.getTime()));
        } else{
            s.setDate(9, null);
        }
        s.setInt(10, customerId);
        s.setInt(11, id);
        return s;
    }

    /**
     * Doplni udaje do SQL dopytu pre vlozenie projektu
     * @param s - prepared statement s
     * @return - doplneny SQL dopyt
     * @throws SQLException vynimka pri chybe SQL dopytu
     */
    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        return null;
    }
}