package app.service;

import app.config.DbContext;
import app.db.ProjectReminder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectReminderService  extends Service<ProjectReminder> {

    private static ProjectReminderService instance = new ProjectReminderService();
    public static ProjectReminderService getInstance(){ return  instance;}

    public List<ProjectReminder> getAllReminders() throws SQLException {

        String sql = "select r.id,text, project_id,closed,date,p.projectNumber,sent " +
                "from reminders r left join projects p " +
                "on r.project_id = p.id";
        return findAll(sql);

    }
    public List<ProjectReminder> getActiveReminders() throws SQLException {

        String sql = "select r.id,text, project_id,closed,date,p.projectNumber,sent " +
                "from reminders r left join projects p " +
                "on r.project_id = p.id " +
                "where closed = false";
        return findAll(sql);

    }
    public List<ProjectReminder> getAllNotSentReminders() throws SQLException {
        String sql = "select r.id,text, project_id,closed,date,p.projectNumber,sent " +
                "from reminders r left join projects p " +
                "on r.project_id = p.id " +
                "where closed = false and sent = false";
        return findAll(sql);
    }
    public List<ProjectReminder> getAllNotSentRemindersByUser(Integer user_id) throws SQLException {
        String sql = "select r.id,text, r.project_id,closed,date,p.projectNumber,sent " +
                "from reminders r left join projects p " +
                "on r.project_id = p.id left join administration a " +
                " on p.id = a.project_id "+
                "where closed = false and sent = false and a.user_id = ?";
        PreparedStatement s = DbContext.getConnection().prepareStatement(sql);
        s.setInt(1,user_id);

        return findAll(s);
    }
    public List<ProjectReminder> getActiveRemindersByUser(Integer user_id) throws SQLException {

        String sql = "select r.id,text, r.project_id,closed,date,p.projectNumber,sent " +
                "from reminders r left join projects p " +
                "on r.project_id = p.id left join administration a " +
                " on p.id = a.project_id "+
                "where closed = false and a.user_id = ?";
        PreparedStatement s = DbContext.getConnection().prepareStatement(sql);
        s.setInt(1,user_id);

        return findAll(s);

    }



    @Override
    public ProjectReminder Objekt(ResultSet r) throws SQLException {
        ProjectReminder p = new ProjectReminder();
        p.setId(r.getInt("id"));
        p.setText(r.getString("text"));
        p.setProject_id(r.getInt("project_id"));
        p.setIsClosed(r.getBoolean("closed"));
        p.setDate(r.getDate("date"));
        p.setProjectNumber(r.getString("projectNumber"));
        p.setSent(r.getBoolean("sent"));
        return p;
    }
}
