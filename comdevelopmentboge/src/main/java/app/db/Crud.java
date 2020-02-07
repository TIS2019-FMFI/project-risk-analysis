package app.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class Crud<T> {

    /**
     * Abstraktne metody pre vlozenie, akzualizovanie a vymazanie entity
     * @param s SQL dopyt
     * @return SQL dopyt
     * @throws SQLException ak nastane chyba pri vykovavani SQL dopytu
     */
    public abstract PreparedStatement fill(PreparedStatement s) throws SQLException;
    public abstract PreparedStatement fillInsert(PreparedStatement s) throws SQLException;

    /**
     * Vlozenie entity do tabulky.
     * @param sql SQL dopyt
     * @param cisloStlpca cislo vrateneho stlpca, teda vratene id
     * @return cislo slpca, teda id
     * @throws SQLException ak nastane chyba pri vykovavani SQL dopytu
     */
    public Integer insert(PreparedStatement sql, Integer cisloStlpca) throws SQLException {
        PreparedStatement s = fillInsert(sql);
        s.executeUpdate();
        try(ResultSet r = sql.getGeneratedKeys()) {
            r.next();
            return r.getInt(cisloStlpca);
        }
    }

    /**
     * Aktualizovanie entity v tabulke.
     * @param sql SQL dopyt
     * @throws SQLException ak nastane chyba pri vykovavani SQL dopytu
     */
    public void update(PreparedStatement sql) throws SQLException{
        PreparedStatement s = fill(sql);
        s.executeUpdate();
    }

    /**
     * Vymazanie entity z tabulky.
     * @param sql SQL dopyz
     * @param projectId id entity, ktora sa vymaze
     * @throws SQLException
     */
    public void delete(PreparedStatement sql, Integer projectId) throws SQLException {
        sql.setInt(1, projectId);
        sql.executeUpdate();
    }
}
