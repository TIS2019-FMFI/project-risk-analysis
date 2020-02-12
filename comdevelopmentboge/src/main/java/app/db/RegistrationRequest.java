package app.db;

import app.config.DbContext;
import app.exception.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Obsahuje udaje o poziadavke na registraciu z databazy
 */
public class RegistrationRequest extends Crud<RegistrationRequest>{
    /**
     * Text, ktory sa zobrazi na hlavnej stranke
     */
    private String text;

    /**
     * ID uzivatela z tabulky USERS, ktory ziada o registraciu
     */
    private Integer user_id;

    /**
     * Konstuktor objektu typu RegistrationRequest
     */
    public RegistrationRequest(){}

    /**
     * Nastavenie atributov objektu typu RegistrationRequest
     */

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
     * Vlozi ziadost do tabulky
     * @throws SQLException chyba pri ziskavani dat z databazy
     */
    public void insert() throws SQLException {
        String sql = "INSERT INTO registration_requests(user_id,text) VALUES(?,?) ";
        insert(DbContext.getConnection().prepareStatement(sql),null);

    }

    /**
     * Vymaze ziadost z tabulky na zaklade ID pouzivatela
     * @throws SQLException chyba pri ziskavani dat z databazy
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
     * Doplni udaje do SQL dopytu pre vlozenie ziadosti
     * @param s - prepared statement s
     * @return - doplneny SQL dopyt
     * @throws SQLException vynimka pri chybe SQL dopytu
     */
    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        s.setInt(1, user_id);
        s.setString(2, text);
        return s;
    }
}
