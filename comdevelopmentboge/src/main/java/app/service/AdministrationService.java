package app.service;

import app.config.DbContext;
import app.db.Administration;
import app.db.Project;
import app.db.User;
import app.exception.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministrationService extends Service<Administration>{

    private static AdministrationService administrationService = new AdministrationService();
    public static AdministrationService getAdministrationService(){ return  administrationService;}

    @Override
    public Administration Objekt(ResultSet r) throws SQLException {
        Administration a = new Administration();
        a.setId(r.getInt("id"));
        a.setUserId(r.getInt("user_id"));
        a.setProjectId(r.getInt("project_id"));
        return a;
    }

    public List<Project> findProjectsByAdmin(String fullName) throws SQLException {
        Integer userId = UserService.getInstance().findUserByFullName(fullName).getId();
        String sql = "select project_id from administration where user_id = ?";
        List<Project> projects = new ArrayList<>();

        try (PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer project_id = rs.getInt(1);
                Project project = ProjectService.getProjectService().findProjectById(project_id);
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public Administration findAdministrationByProjectId(Integer projectId) throws DatabaseException, SQLException {
        return super.findById(projectId,"SELECT * FROM administration WHERE project_id = ?");
    }

    public Administration findAdministrationByProjectNum(String projectNum) throws DatabaseException, SQLException {
        return super.findByName(projectNum,"SELECT * FROM administration as a join projects as p on a.project_id = p.id WHERE p.projectNumber = ?");
    }

}
