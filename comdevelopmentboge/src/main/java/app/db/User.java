package app.db;


import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    public enum USERTYPE {
        CENTRAL_ADMIN,PROJECT_ADMIN,USER,FEM
    }
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private USERTYPE userType;
    private Boolean deleted;
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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {

            s.setString(1, name);
            s.setString(2, surname);
            s.setString(3, email);
            s.setString(4, password);
            s.setString(5, userType.toString());
            s.setBoolean(6,approved);
            s.setBoolean(7,deleted);


            if  (s.executeUpdate() > 0) {
                id = getLastInsertedID();
            }

        }
    }

    public void update() throws SQLException {
        String sql = "UPDATE users SET name = ?, surname = ?, email = ?, password = ?, userType = ?, approved = ?, deleted = ? WHERE id = ?;";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {

            s.setString(1, name);
            s.setString(2, surname);
            s.setString(3, email);
            s.setString(4, password);
            s.setString(5, userType.toString());
            s.setBoolean(6,approved);
            s.setBoolean(7,deleted);
            s.setInt(8, id);


            s.executeUpdate();
        }

    }

    private Integer getLastInsertedID() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID();";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {

            try  (ResultSet r = s.executeQuery() ) {
                if (r.next()) {
                    return r.getInt(1);
                }
                return null;
            }

        }
    }



}
