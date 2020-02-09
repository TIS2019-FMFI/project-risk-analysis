package app.db;

import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
        super.delete(DbContext.getConnection().prepareStatement("DELETE FROM administration WHERE project_id = ?"), project_id);
    }

    public void update() throws SQLException {
        if(id == null) {
            throw new IllegalStateException("Id nie je nastavene");
        }
        super.update(DbContext.getConnection().prepareStatement("UPDATE administration SET user_id = ?, " +
                "project_id = ? WHERE id = ?"));
    }

    /**
     * Nastavi parametre SQL dopytu a vlozi citatela do tabulky administration
     * @throws SQLException ak nastane chyba pri vykovavani SQL dopytu
     */
    public void insert() throws SQLException {
        id = super.insert(DbContext.getConnection().prepareStatement("INSERT INTO administration " +
                "(user_id, project_id) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS), 1);

    }

    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        s.setInt(1, user_id);
        s.setInt(2, project_id);
        s.setInt(3, id);
        return s;
    }

    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        s.setInt(1, user_id);
        s.setInt(2, project_id);
        return s;
    }
}
