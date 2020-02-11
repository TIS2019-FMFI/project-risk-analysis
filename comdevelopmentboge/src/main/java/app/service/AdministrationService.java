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

    /**
     * Getter a setter instancie triedy AdministrationService
     */
    private static AdministrationService administrationService = new AdministrationService();
    public static AdministrationService getAdministrationService(){ return  administrationService;}

    /**
     * Nastavenie objektu AdministrationService
     * @param r vysledok dopytu z databazy
     * @return objekt AdministrationService
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    @Override
    public Administration Objekt(ResultSet r) throws SQLException {
        Administration a = new Administration();
        a.setId(r.getInt("id"));
        a.setUserId(r.getInt("user_id"));
        a.setProjectId(r.getInt("project_id"));
        return a;
    }

    /**
     * Ziskanie projektov, ktorych je adminom uzivatel s danym emailom
     * @param email email uzivatela, ktory je adminom projektov
     * @return zoznam projektov
     * @throws SQLException chyba pri vykovania SQL dopytu
     */
    public List<Project> findProjectsByAdmin(String email) throws SQLException {
        String sql = "select a.project_id from administration as a join users as u on a.user_id = u.id where u.email = ?";
        List<Project> projects = new ArrayList<>();

        try (PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, email);

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


    /**
     * Ziskanie zaznamu z tabulky administratiom, kde projekt ma dane cislo
     * @param projectNum projektove cislo
     * @return zaznam z tabulky, objekt typu Administration
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    public Administration findAdministrationByProjectNum(String projectNum) throws SQLException {
        return super.findByName(projectNum,"SELECT * FROM administration as a join projects as p on a.project_id = p.id WHERE p.projectNumber = ?");
    }

}
