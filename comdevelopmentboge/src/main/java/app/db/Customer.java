package app.db;

import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer extends Crud<Customer>{
    /**
     * Objekt typu zakaznik
     */

    /**
     * id - id zakaznika
     * name - nazov zakaznika
     */
    private Integer id;
    private String name;

    /**
     * Konstruktor zakaznika
     */
    public Customer(){};

    /**
     * Konstruktor zakaznika s parametrami
     * @param id id zakaznika
     * @param name nazov zakaznika
     */
    public Customer(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Ziskannie id zakaznika
     * @return id zakaznika
     */
    public Integer getId() {
        return id;
    }

    /**
     * Nastavenie id zakaznika
     * @param id id zakaznika, ktore sa nazstavi
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Ziskanie nazvu zakaznika
     * @return nazov zakaznika
     */
    public String getName() {
        return name;
    }

    /**
     * Nastavenie nazvu zakaznika
     * @param name nazov zakaznika
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Ziskanie zakaznika, typu String
     * @return String nazov zakaznika
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * Vlozenie zakaznika do tabulky
     * @return vlozeny zakaznik
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    public Integer insert() throws SQLException {
        String sql = "insert into customers (name) values (?)";
        return super.insert(DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS), 1);
    }

    /**
     * Nastavenie objektu zakaznik pri aktualizovani/vymazani
     * @param s SQL dopyt
     * @return
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        return null;
    }

    /**
     * Nastavenie typu zakaznik pri vlozeni
     * @param s
     * @return
     * @throws SQLException chyba pri vykovani SQL dopytu
     */
    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        s.setString(1, name);
        return s;
    }
}
