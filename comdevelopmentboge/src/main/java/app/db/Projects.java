package app.db;

import java.math.BigDecimal;
import java.util.Date;

public class Projects {
    private Integer id;
    private String projectNumber;
    private String projectName;
    private String customer;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}