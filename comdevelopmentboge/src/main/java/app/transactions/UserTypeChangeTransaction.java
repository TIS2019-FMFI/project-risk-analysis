package app.transactions;

import app.config.DbContext;
import app.config.SendMail;
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserTypeChangeTransaction {


    /**
     * Transakcia zmeny roly uzivatela
     * @param user - uzivatel, ktoreho rolu chceme zmenit
     * @param usertype - rola, ktoru chceme uzivatelovi pridelit
     * @throws SQLException chyba pri ziskavani dat z databazy
     */
    public static void changeUserType(User user, USERTYPE usertype) throws SQLException, MessagingException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        USERTYPE previousUserType = user.getUserTypeU();
        try {
            String userT = user.getUserTypeSlovak();
            user.setUserType(usertype);
            user.update();
            //commitne zmeny do databazy

            LogService.createLog("Zmena roly užívatela " + user.getFullName());
            SendMail.sendRoleChange(user.getEmail(), "Rola užívateľa bola zmenená z <strong>" + userT + "</strong> na <strong>" + user.getUserTypeSlovak() + ".</strong>");
            DbContext.getConnection().commit();
            MyAlert.showSuccess("Rola užívateľa " + user.getFullName() + " bola úspešne zmenená");
        } catch (SQLException | MessagingException e) {
            user.setUserType(previousUserType);
            MyAlert.showSuccess("Rolu užívateľa " + user.getFullName() + " sa nepodarilo zmeniť");
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    /**
     * Transakcia zmeny roly a projektov uzivatela
     * @param user - pouzivatel ktoremu chceme zmenit rolu
     * @param projectsToAdd - projekty, ktore chceme uzivatelovi pridelit
     * @param projectsToDelete - projekty, ktore chceme uzivatelovi vymazat
     * @throws DatabaseException - chyba pri spracovani v databaze
     * @throws SQLException - chyba pri vykonavani SQL dopytu
     */
    public static void changeProjects(User user, List<String> projectsToAdd, List<String> projectsToDelete) throws DatabaseException, SQLException, MessagingException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        USERTYPE previousUserType = user.getUserTypeU();
        try {
            String userT = user.getUserTypeSlovak();
            user.setUserType(USERTYPE.PROJECT_ADMIN);

            StringBuilder pToDel = new StringBuilder();
            String prefix = "";
            for (String del : projectsToDelete) {
                pToDel.append(prefix);
                prefix = ", ";
                pToDel.append(del);
                Administration a = AdministrationService.getAdministrationService().findAdministrationByProjectNum(del);
                if (a != null) {
                    a.delete();
                }
            }
            StringBuilder pToAdd = new StringBuilder();
            prefix = "";
            for (String add : projectsToAdd) {
                pToAdd.append(prefix);
                prefix = ", ";
                pToAdd.append(add);

                Project project = ProjectService.getProjectService().findProjectByNum(add);
                Administration a = new Administration();
                a.setProjectId(project.getId());
                a.setUserId(user.getId());
                a.insert();
            }

            user.update();

            String msg2 = "";
            String msg1 = "";
            if (projectsToAdd.size() != 0 && projectsToDelete.size() == 0) {
                msg2 = "<strong>Pridané projekty: </strong> " + pToAdd.toString();
            }
            if (projectsToAdd.size() == 0 && projectsToDelete.size() != 0) {
                msg2 = "<strong>Odstránené projekty: </strong> " + pToDel.toString();
            }
            if (projectsToAdd.size() != 0 && projectsToDelete.size() != 0) {
                msg2 = "<strong>Pridané projekty: </strong> " + pToAdd.toString() + "<br> <strong>Odstránené projekty: </strong> " + pToDel.toString();
            }
            if (!previousUserType.equals(user.getUserTypeU())) {
                msg1 = "Rola užívateľa bola zmenená z <strong>" + userT + "</strong> na <strong>" + user.getUserTypeSlovak() + ".</strong>";
                LogService.createLog("Zmena projektov užívatela " + user.getFullName());
            } else {
                LogService.createLog("Zmena roly a projektov užívatela " + user.getFullName());
            }
            SendMail.sendRoleProjectsChange(user.getEmail(), msg1 + " <br> " + msg2);
            DbContext.getConnection().commit();
            MyAlert.showSuccess("Rola a projekty užívateľa " + user.getFullName() + " boli úspešne zmenené");
        } catch (SQLException | MessagingException e) {
            user.setUserType(previousUserType);
            MyAlert.showError("Rola a projekty užívateľa " + user.getFullName() + "  sa nepodarilo zmeniť");
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }

    }

}
