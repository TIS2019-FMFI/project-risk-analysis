package Tables;

import java.util.Date;

public class FEM {
    private Integer id;
    private String partNumber;
    private String status;
    private Date startDate;
    private Date endDate;
    private Integer weekNumber;

    public FEM(Integer id, String partNumber, String status, Date startDate, Date endDate, Integer weekNumber) {
        this.id = id;
        this.partNumber = partNumber;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekNumber = weekNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }
}
