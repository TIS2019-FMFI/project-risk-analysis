package app.db;
import java.util.Date;

public class Projects {
    private Integer id;
    private Integer projectNumber;
    private String pojectName;
    private Integer partNumber;
    private String ros;
    private String roce;
    private Integer volumes;
    private Integer ddCost;
    private Integer prototypeCost;
    private Date lastUpdated;

    public Projects(Integer id, Integer projectNumber, String pojectName, Integer partNumber, String ros, String roce, Integer volumes, Integer ddCost, Integer prototypeCost, Date lastUpdated) {
        this.id = id;
        this.projectNumber = projectNumber;
        this.pojectName = pojectName;
        this.partNumber = partNumber;
        this.ros = ros;
        this.roce = roce;
        this.volumes = volumes;
        this.ddCost = ddCost;
        this.prototypeCost = prototypeCost;
        this.lastUpdated = lastUpdated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(Integer projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getPojectName() {
        return pojectName;
    }

    public void setPojectName(String pojectName) {
        this.pojectName = pojectName;
    }

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
