package app.transactions;

import app.config.DbContext;
import app.db.Administration;
import app.db.Project;
import app.db.User;
import app.db.User.USERTYPE;
import app.exception.DatabaseException;
import app.exception.MyException;
import app.gui.MyAlert;
import app.service.AdministrationService;
import app.service.LogService;
import app.service.ProjectService;

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
            LogService.createLog("Zmena roly užívateľa " + user.getFullName());
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
     * @param projects - pridelené projekty
     * @throws DatabaseException
     * @throws SQLException
     */
    public static void changeProjects(User user, List<Project> projects) throws DatabaseException, SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        USERTYPE previousUserType = user.getUserTypeU();
        try {
            user.setUserType(USERTYPE.PROJECT_ADMIN);
            user.update();

            for(Project project : projects) {
                Administration a = AdministrationService.getAdministrationService().findAdministrationByProjectId(project.getId());
                a.delete();
            }
            DbContext.getConnection().commit();
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

    //TODO
    public static void insertProject(User user, String projectNum) throws DatabaseException, SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            Project project = ProjectService.getProjectService().findProjectByProjectNumber(projectNum);
            if(project == null) {
                throw new MyException("Projekt sa nepodarilo vložiť");
            }
            Administration a = AdministrationService.getAdministrationService().findAdministrationByProjectId(project.getId());
            if(a.getUserId().equals(user.getId())) {
                throw new MyException("Projekt má už užívateľ pridelený");
            }
            if(a.getUserId() != null) {
                throw new MyException("Projekt má iného admina");
            }

            a.setUserId(user.getId());

            DbContext.getConnection().commit();

        } catch (SQLException e) {
            MyAlert.showError("Projekt sa nepodarilo vložiť");
            DbContext.getConnection().rollback();
            throw e;
        } catch (MyException e) {
            MyAlert.showError(e.getMessage());
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }

    }




}
