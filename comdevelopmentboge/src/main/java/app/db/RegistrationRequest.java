package app.db;

import app.config.DbContext;
import app.exception.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Obsahuje údaje o požiadavke na registráciu z databázy
 */
public class RegistrationRequest extends Crud<RegistrationRequest>{
    /**
     * Text, ktorý sa zobrazí na hlavnej stránke
     */
    private String text;

    /**
     * ID úžívateľa z tabuľy USERS, ktorý žiada o registráciu
     */
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

    /**
     * Vloží žiadosť do tabuľky
     * @throws SQLException
     */
    public void insert() throws SQLException {
        String sql = "INSERT INTO registration_requests(user_id,text) VALUES(?,?) ";
        insert(DbContext.getConnection().prepareStatement(sql),null);

    }

    /**
     * Vymaže žiadosť z tabuľky na základe ID používateľa
     * @throws SQLException
     */
    public void delete() throws SQLException {
        String sql = "DELETE FROM registration_requests WHERE user_id = ?";
        delete(DbContext.getConnection().prepareStatement(sql), user_id);

    }


    @Override
    public PreparedStatement fill(PreparedStatement s)  {
        return null;
    }

    /**
     * Doplní údaje do SQL dopytu pre vloženie žiadosti
     * @param s
     * @return
     * @throws SQLException
     */
    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        s.setInt(1, user_id);
        s.setString(2, text);
        return s;
    }
}
