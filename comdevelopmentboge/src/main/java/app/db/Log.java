package app.db;
import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class Log {
    private Integer id;
    private Integer userId;
    private Timestamp time;
    private String text;


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
