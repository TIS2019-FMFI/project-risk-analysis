package app.db;

import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Administration extends Crud<Administration>{

    private Integer id;
    private Integer user_id;
    private Integer project_id;

    public Administration(){};


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer userId) {
        this.user_id = userId;
    }

    public Integer getProjectId() {
        return user_id;
    }

    public void setProjectId(Integer project_id) {
        this.project_id = project_id;
    }

    public void delete() throws SQLException {
        if (project_id == null) {
            throw new IllegalStateException("project_id nie je nastavene");
        }
        System.out.println("del I" + getId());
        System.out.println("del PI" + getProjectId());
        System.out.println("del UI" + getUserId());
        super.delete(DbContext.getConnection().prepareStatement("DELETE FROM administration WHERE project_id = ?"), project_id);
    }

    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        s.setInt(1, id);
        s.setInt(2, user_id);
        s.setInt(3, project_id);
        return s;
    }

    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        s.setInt(2, user_id);
        s.setInt(3, project_id);
        return s;
    }
}
