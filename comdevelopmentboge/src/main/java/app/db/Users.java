package app.db;


public class Users {
    public enum USERTYPE {
        ADMIN,PROJECT_ADMIN,USER,FEM
    }
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private USERTYPE userType;
    private Boolean deleted;
    private Boolean approved;


    public Users(){}

    public Users(Integer id,String name, String surname, String email, String password, USERTYPE userType, Boolean deleted, Boolean approved) {
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

    public Integer insert() {
        //TODO insert user and return ID
        //ak tam ale bude uzivatel s danym menom tak musi vyhodit chybu
        return 0;
    }



}
