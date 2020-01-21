package app.service;

import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectAdministrationService {

    private ProjectAdministrationService(){};
    private static ProjectAdministrationService projectAdministrationService = new ProjectAdministrationService();
    public static ProjectAdministrationService getInstance(){return projectAdministrationService;}

    public List<Integer> getProjectAdminIds(String projectDef){
        String sql = "select u.id from projects p inner join administration a on p.id=a.project_id inner join users u on a.user_id=u.id where p.projectNumber=?";
        List<Integer> adminIds = new ArrayList<>();

        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){
            preparedStatement.setString(1, projectDef);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                adminIds.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminIds;
    }
}
