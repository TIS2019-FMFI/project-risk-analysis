package app.db;
import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Log extends Crud<Log> {
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
        try {
            insert(DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS), 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        s.setInt(1,userId);
        s.setTimestamp(2, time);
        s.setString(3, text);
        return s;
    }
}
