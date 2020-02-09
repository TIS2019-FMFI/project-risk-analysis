package app.transactions;

import app.config.DbContext;
import app.db.Administration;
import app.db.Project;
import app.db.User;
import app.db.User.USERTYPE;
import app.exception.DatabaseException;
import app.exception.MyException;
import app.gui.MyAlert;
import app.gui.administration.DialogProjectsController;
import app.service.AdministrationService;
import app.service.LogService;
import app.service.ProjectService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserTypeChangeTransaction {


    /**
     * Transakcia zmeny roly užívateľa
     * @param user - užívateľ, ktorého rolu chceme zmeniť
     * @param usertype - rola, ktorú chceme užívateľovi prideliť
     * @throws SQLException
     */
    public static void changeUserType(User user, USERTYPE usertype) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        USERTYPE previousUserType = user.getUserTypeU();
        try {
            user.setUserType(usertype);
            user.update();
            //commitne zmeny do databazy
            DbContext.getConnection().commit();

            MyAlert.showSuccess("Rola užívateľa " + user.getFullName() + " bola úspešne zmenená");
            LogService.createLog("Zmena roly užívatela " + user.getFullName());
        } catch (SQLException e) {
            user.setUserType(previousUserType);
            MyAlert.showSuccess("Rolu užívateľa " + user.getFullName() + " sa nepodarilo zmeniť");
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    /**
     * Transakcia zmeny roly a projektov užívateľa
     * @param user - užívateľ, ktorému chceme zmeniť rolu a projekty
     * @throws DatabaseException
     * @throws SQLException
     */
    public static void changeProjects(User user, List<String> projectsToAdd, List<String> projectsToDelete) throws DatabaseException, SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        USERTYPE previousUserType = user.getUserTypeU();
        try {
            user.setUserType(USERTYPE.PROJECT_ADMIN);


            for(String del : projectsToDelete) {
                Administration a = AdministrationService.getAdministrationService().findAdministrationByProjectNum(del);
                if (a != null) {
                    a.delete();
                }
            }

            for(String add : projectsToAdd) {
                Project project = ProjectService.getProjectService().findProjectByNum(add);
                Administration a = new Administration();
                a.setProjectId(project.getId());
                a.setUserId(user.getId());
                a.insert();
            }

            user.update();

            DbContext.getConnection().commit();
            if(previousUserType.equals(user.getUserTypeU())) {
                LogService.createLog("Zmena projektov užívatela " + user.getFullName());
            }
            else {
                LogService.createLog("Zmena roly a projektov užívatela " + user.getFullName());
            }
            MyAlert.showSuccess("Rola a projekty užívateľa " + user.getFullName() + " boli úspešne zmenené");
        } catch (SQLException e) {
            user.setUserType(previousUserType);
            MyAlert.showError("Rola a projekty užívateľa " + user.getFullName() + "  sa nepodarilo zmeniť");
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }

    }





}
