package app.db;
import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Log {
    private Integer id;
    private Integer userId;
    private Timestamp time;
    private String text;
    private String userFirstName;
    private String userLastName;

    public Log(){}

    public Log(Integer id, Integer userId, Timestamp time, String text) {
        this.id = id;
        this.userId = userId;
        this.time = time;
        this.text = text;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }


    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }


    public void insert(){
        String sql = "insert into logs (user_id, time, text) values (?,?,?)";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)) {

            preparedStatement.setInt(1,userId);
            preparedStatement.setTimestamp(2, time);
            preparedStatement.setString(3, text);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
