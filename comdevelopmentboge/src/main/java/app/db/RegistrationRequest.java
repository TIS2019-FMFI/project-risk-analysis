package app.db;

import app.config.DbContext;
import app.exception.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationRequest {
    private String text;
    private Integer user_id;

    public RegistrationRequest(){}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void insert() throws SQLException, DatabaseException {
        String sql = "INSERT INTO registration_requests(user_id,text) VALUES(?,?) ";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {

            s.setInt(1, user_id);
            s.setString(2, text);

            if (s.executeUpdate() < 0) {
                throw new DatabaseException("Nepodarilo sa vlozit riadok do tabulky");
            }

        }
    }
    public void delete() throws SQLException, DatabaseException {
        String sql = "DELETE FROM registration_requests WHERE user_id = ?";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {

            s.setInt(1, user_id);

            if (s.executeUpdate() < 0) {
                throw new DatabaseException("Nepodarilo sa odstranit riadok z tabulky");
            }

        }
    }
}
