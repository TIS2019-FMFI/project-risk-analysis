package app.db;
import java.util.Date;

public class Reminder {

    public enum REMINDERTYPE {
        PROJECT,FEM
    }

    private Integer id;
    private String text;
    private Integer partNumber;
    private Date timePeriod;
    private Boolean closed;
    private Boolean minimized;
    private REMINDERTYPE remindertype;

    public Reminder() {};

    public Reminder(Integer id, String text, Integer partNumber, Date timePeriod) {
        this.id = id;
        this.text = text;
        this.partNumber = partNumber;
        this.timePeriod = timePeriod;
    }

    public Reminder(Integer id, String text) {
        this.id = id;
        this.text = text;
        this.closed = false;
        this.minimized = false;
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

    public Boolean getIsClosed() {
        return closed;
    }

    public void setIsClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getIsMinimized() {
        return minimized;
    }

    public void setIsMinimized(Boolean minimized) {
        this.minimized = minimized;
    }

    public REMINDERTYPE getReminderType() {
        return remindertype;
    }

    public void setReminderType(REMINDERTYPE remindertype) {
        this.remindertype = remindertype;
    }


}
