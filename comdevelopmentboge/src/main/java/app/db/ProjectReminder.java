package app.db;

import app.config.DbContext;
import app.exception.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class ProjectReminder {

    private String projectNumber;
    private Integer project_id;
    private Integer id;
    private String text;
    private Boolean closed;
    private Boolean minimized = false;
    private Date date;
    private String unique_code;


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


    public void insert() throws SQLException, DatabaseException {
        String sql = "INSERT INTO reminders(text,project_id,date,closed,unique_code) VALUES(?,?,?,?,?) on duplicate key update unique_code = unique_code ;";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {

            s.setString(1, getText());
            s.setInt(2, getProject_id());
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            s.setDate(3, date);
            s.setBoolean(4, false);
            s.setString(5,unique_code);


            if (s.executeUpdate() > 0) {
                setId(getLastInsertedID());
            } else {
                throw new DatabaseException("Nepodarilo sa vlozit riadok do tabulky");
            }

        }
    }

    public void update() throws SQLException, DatabaseException {
        String sql = "UPDATE reminders set closed = ? where id = ?";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {

            s.setBoolean(1, getIsClosed());
            s.setInt(2, getId());

            if (s.executeUpdate() < 0) {
                throw new DatabaseException("Nepodarilo sa upravit riadok v tabulke");
            }

        }
    }

    private Integer getLastInsertedID() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID();";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    return r.getInt(1);
                }
                return null;
            }

        }
    }

}
