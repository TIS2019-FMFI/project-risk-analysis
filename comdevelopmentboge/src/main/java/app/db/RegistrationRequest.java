package app.db;

public class RegistrationRequest {
    private Integer id;
    private String text;
    private Integer user_id;

    public RegistrationRequest(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public void insert() {
        //TODO vlozit request do databazy
    }
}
