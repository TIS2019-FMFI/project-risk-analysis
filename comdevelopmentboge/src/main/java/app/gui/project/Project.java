package app.gui.project;

public class Project {
    private Integer id;
    private String projectNumber;
    private String projectName;
    private String customer;
    private String partNumber;
    private String ros;
    private String roce;
    private Integer volumes;
    private Integer ddCost;
    private Integer prototypeCost;

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

    public Integer getVolumes() {
        return volumes;
    }

    public void setVolumes(Integer volumes) {
        this.volumes = volumes;
    }

    public Integer getDdCost() {
        return ddCost;
    }

    public void setDdCost(Integer ddCost) {
        this.ddCost = ddCost;
    }

    public Integer getPrototypeCost() {
        return prototypeCost;
    }

    public void setPrototypeCost(Integer prototypeCost) {
        this.prototypeCost = prototypeCost;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}