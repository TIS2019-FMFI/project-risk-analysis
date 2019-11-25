package app.db;
import java.util.Date;

public class Logs {
    private Integer id;
    private Date date;
    private String time;
    private String text;

    public Logs(Integer id, Date date, String time, String text) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
