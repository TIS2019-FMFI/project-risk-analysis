package app.db;


import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User extends Crud<User>{

    /**
     * Definuje rolu používateľa
     */
    public enum USERTYPE {
        CENTRAL_ADMIN,PROJECT_ADMIN,USER,FEM
    }

    /**
     * Unikátne ID užívateľa z databázy
     */
    private Integer id;

    /**
     * Meno užívateľa
     */
    private String name;

    /**
     * Priezvisko užívateľa
     */
    private String surname;

    /**
     * E-mailova adresa užívateľa
     * Používa sa ako prihlasovacie meno
     */
    private String email;

    /**
     * Heslo pre prihlásenie
     */
    private String password;

    /**
     * Rola užívateľa
     */
    private USERTYPE userType;

    /**
     * Určuje, či je užívateľ vymazaný
     */
    private Boolean deleted;

    /**
     * Určuje, či je užívateľova registrácia schválená a môže používať aplikáciu
     */
    private Boolean approved;


    public User(){}

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType.toString();
    }

    public USERTYPE getUserTypeU() {return userType;}

    public void setUserType(USERTYPE userType) {
        this.userType = userType;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getFullName() {
        return name + " " + surname;
    }

    public void insert() throws SQLException {
        String sql = "INSERT INTO users(name,surname,email,password,userType,approved,deleted) VALUES(?,?,?,?,?,?,?);";
        id = insert(DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS),1);

    }

    public void update() throws SQLException {
        String sql = "UPDATE users SET name = ?, surname = ?, email = ?, password = ?, userType = ?, approved = ?, deleted = ? WHERE id = ?;";
        update(DbContext.getConnection().prepareStatement(sql));

    }
    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        s = fillBase(s);
        s.setInt(8, id);
        return s;
    }

    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        return fillBase(s);
    }

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
