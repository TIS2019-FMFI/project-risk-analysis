package app.db;


import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User extends Crud<User>{
    /**
     * Objekt typu pouzivatel
     */

    /**
     * Definuje rolu pouzivatela
     */
    public enum USERTYPE {
        CENTRAL_ADMIN,PROJECT_ADMIN,USER
    }

    /**
     * Unikatne ID uzivatela z databazy
     */
    private Integer id;

    /**
     * Meno uzivatela
     */
    private String name;

    /**
     * Priezvisko uzivatela
     */
    private String surname;

    /**
     * E-mailova adresa uzivatela
     * Pouziva sa ako prihlasovacie meno
     */
    private String email;

    /**
     * Heslo pre prihlasenie
     */
    private String password;

    /**
     * Rola uzivatela
     */
    private USERTYPE userType;

    /**
     * Urcuje, ci je uzivatel vymazany
     */
    private Boolean deleted;

    /**
     * Urcuje, ci je uzivatelova registracia schvalena a moze pouzivat aplikaciu
     */
    private Boolean approved;


    /**
     * Konstruktor objektu typu pouzivatel
     */
    public User(){}

    /**
     * Konstruktor typu pouzivatel s parametrami
     * @param id unikatne id uzivatela
     * @param name meno uzivatela
     * @param surname priezvisko uzivatela
     * @param email email uzivatela
     * @param password heslo uzivatela
     * @param userType rola uzivatela
     * @param deleted je vymazany
     * @param approved je schvaleny
     */
    public User(Integer id,String name, String surname, String email, String password, USERTYPE userType, Boolean deleted, Boolean approved) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.deleted = deleted;
        this.approved = approved;
    }

    /**
     * Ziskanie id uzivatela
     * @return id uzivatela
     */
    public Integer getId() {
        return id;
    }

    /**
     * Nastavenie id uzivatela
     * @param id id uzivatela, ktore sa nastavi
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Ziskanie mena uzivatela
     * @return meno uzivatela
     */
    public String getName() {
        return name;
    }

    /**
     * Nastavenie mena uzivatela
     * @param name meno uzivatela, ktore sa nastavi
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Ziskanie priezviska uzivatela
     * @return priezvisko uzivatela
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Nastavenie priezviska uzivatela
     * @param surname priezvisko uzivatela, ktore sa nastavi
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Ziskanie emailu uzivatela
     * @return email uzivatela
     */
    public String getEmail() {
        return email;
    }

    /**
     * Nastavenie emailu uzivatela
     * @param email emailu uzivatela, ktory sa nastavi
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Ziskanie hesla uzivatela
     * @return heslo uzivatela
     */
    public String getPassword() {
        return password;
    }

    /**
     * Nastavenie hesla uzivatela
     * @param password heslo uzivatela, ktore sa nastavi
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Ziskanie typu uzivatela - String
     * @return typ uzivatela
     */
    public String getUserType() {
        return userType.toString();
    }

    /**
     * Ziskanie typu uzivatela - USERTYPE
     * @return typ uzivatela
     */
    public USERTYPE getUserTypeU() {return userType;}

    /**
     * Nastavenie typu uzivatela
     * @param userType typ uzivatela, ktory sa nastavi
     */
    public void setUserType(USERTYPE userType) {
        this.userType = userType;
    }

    /**
     * Zistenie, ci je uzivatel vymazany
     * @return true alebo false
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * Nastavenie, ci je uzivatel vymazany
     * @param deleted true alebo false
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Zistenie, ci je uzivatel schvaleny
     * @return true alebo false
     */
    public Boolean getApproved() {
        return approved;
    }

    /**
     * Nastavenie, ci je uzivatel schvaleny
     * @param approved true alebo false
     */
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    /**
     * Ziskanie celeho mena uzivatela
     * @return cele meno uzivatela
     */
    public String getFullName() {
        return name + " " + surname;
    }

    /**
     * Vlozenie uzivatela do tabulky
     * @throws SQLException
     */
    public void insert() throws SQLException {
        String sql = "INSERT INTO users(name,surname,email,password,userType,approved,deleted) VALUES(?,?,?,?,?,?,?);";
        id = insert(DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS),1);

    }

    /**
     * Zmena udajov uzivatela v tabulke
     * @throws SQLException
     */
    public void update() throws SQLException {
        String sql = "UPDATE users SET name = ?, surname = ?, email = ?, password = ?, userType = ?, approved = ?, deleted = ? WHERE id = ?;";
        update(DbContext.getConnection().prepareStatement(sql));

    }

    /**
     * Nastavenie objektu typu uzivatel pri update/delete
     * @param s SQL dopyt
     * @return
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        s = fillBase(s);
        s.setInt(8, id);
        return s;
    }

    /**
     * Nastavenie objektu typu uzivatel pri insert
     * @param s
     * @return
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        return fillBase(s);
    }

    /**
     * Nastavenie objektu typu uzivatel
     * @param s
     * @return
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    public PreparedStatement fillBase(PreparedStatement s) throws SQLException {
        s.setString(1, name);
        s.setString(2, surname);
        s.setString(3, email);
        s.setString(4, password);
        s.setString(5, userType.toString());
        s.setBoolean(6,approved);
        s.setBoolean(7,deleted);
        return s;
    }





}
