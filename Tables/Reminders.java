package Tables;

import java.util.Date;

public class Reminders {
    private Integer id;
    private String text;
    private Integer partNumber;
    private Date timePeriod;

    public Reminders(Integer id, String text, Integer partNumber, Date timePeriod) {
        this.id = id;
        this.text = text;
        this.partNumber = partNumber;
        this.timePeriod = timePeriod;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public Date getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(Date timePeriod) {
        this.timePeriod = timePeriod;
    }
}
