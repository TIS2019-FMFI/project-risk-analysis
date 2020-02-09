package app.db;

import app.config.DbContext;
import app.exception.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegistrationRequest extends Crud<RegistrationRequest>{
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

    public void insert() throws SQLException {
        String sql = "INSERT INTO registration_requests(user_id,text) VALUES(?,?) ";
        insert(DbContext.getConnection().prepareStatement(sql),null);

    }
    public void delete() throws SQLException {
        String sql = "DELETE FROM registration_requests WHERE user_id = ?";
        delete(DbContext.getConnection().prepareStatement(sql), user_id);

    }

    @Override
    public PreparedStatement fill(PreparedStatement s)  {
        return null;
    }

    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        s.setInt(1, user_id);
        s.setString(2, text);
        return s;
    }
}
