package app.db;


public class Users {
    private Integer id;
    private String surname;
    private String email;
    private String password;
    private String userType;
    private Boolean deleted;
    private Boolean approved;


    public Users(){};

    public Users(Integer id, String surname, String email, String password, String userType, Boolean deleted, Boolean approved) {
        this.id = id;
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
        return userType;
    }

    public void setUserType(String userType) {
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
}
