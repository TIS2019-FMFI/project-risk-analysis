package app.service;

import app.config.DbContext;
import app.config.SignedUser;
import app.db.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class LogService {

    private LogService(){}
    private static LogService logService = new LogService();
    public static LogService getInstance(){return logService;}

    public ArrayList<Log> findLogsByDate(Date date){
        return null;
    }

    public ArrayList<Log> getAllLogs(){
        ArrayList<Log> result = new ArrayList<>();
        try(PreparedStatement st = DbContext.getConnection().prepareStatement("select logs.*,users.name, users.surname  from logs inner join users on users.id=logs.user_id order by time desc")){

            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Log log = new Log();
                log.setId(rs.getInt(1));
                log.setUserId(rs.getInt(2));
                log.setTime(rs.getTimestamp(4));
                log.setText(rs.getString(3));
                log.setUserFirstName(rs.getString(5));
                log.setUserLastName(rs.getString(6));

                result.add(log);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void createLog(String text) throws SQLException {
        Log l = new Log();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        l.setUserId(SignedUser.getUser().getId());
        l.setText(text);
        l.setTime(currentTime);
        l.insert();
    }
}
