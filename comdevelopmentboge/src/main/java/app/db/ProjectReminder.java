package app.db;

import app.config.DbContext;
import app.exception.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

public class ProjectReminder extends Crud<ProjectReminder> {

    private String projectNumber;
    private Integer project_id;
    private Integer id;
    private String text;
    private Boolean closed;
    private Boolean minimized = false;
    private Date date;
    private String unique_code;
    private Boolean sent;


    public ProjectReminder() {
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

    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public Integer getProject_id() {
        return this.project_id;
    }

    public void setProject_id(final Integer project_id) {
        this.project_id = project_id;
    }

    public String getUnique_code() {
        return this.unique_code;
    }

    public void setUnique_code(final String unique_code) {
        this.unique_code = unique_code;
    }

    public Boolean getSent() {
        return this.sent;
    }

    public void setSent(final Boolean sent) {
        this.sent = sent;
    }


    public void insert() throws SQLException {
        String sql = "INSERT INTO reminders(text,project_id,date,closed,unique_code,sent) VALUES(?,?,?,?,?,?) on duplicate key update unique_code = unique_code ;";
        id = insert(DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS),1);

    }

    public void update() throws SQLException {
        String sql = "UPDATE reminders set closed = ? , sent = ? where id = ?";
        update(DbContext.getConnection().prepareStatement(sql));

    }

    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        s.setBoolean(1, getIsClosed());
        s.setBoolean(2,getSent());
        s.setInt(3, getId());
        return s;
    }

    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        s.setString(1, text);
        s.setInt(2, project_id);
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        s.setDate(3, date);
        s.setBoolean(4, false);
        s.setString(5, unique_code);
        s.setBoolean(6,sent);
        return s;
    }
}
