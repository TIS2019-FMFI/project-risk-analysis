package app.transactions;

import app.App;
import app.config.DbContext;
import app.config.SendMail;
import app.config.SignedUser;
import app.db.ProjectCosts;
import app.db.ProjectReminder;
import app.db.User;
import app.exception.ConfigurationException;
import app.exception.DatabaseException;
import app.exception.GmailMessagingException;
import app.service.ProjectCostsService;
import app.service.ProjectReminderService;
import app.service.UserService;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReminderTransaction {
    public static void loadReminders() throws SQLException, DatabaseException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            List<ProjectReminder> result = new ArrayList<>();

            //najprv najde kody vsetkych remindrov ktore su uz v databaze
            Set<String> old = ProjectReminderService.getInstance().getAllReminders().stream().map(rem -> rem.getUnique_code()).collect(Collectors.toSet());
            System.out.println(old);
            //najde projekty, ktore sa blizia k prekroceniu DDCosts
            List<ProjectCosts> DDCosts = ProjectCostsService.getInstance().getDDCosts();

            //na zaklade danych hodnot vytvori reminders
            result.addAll(createReminders(DDCosts));

            //najde projekty, ktore sa blizia k prekroceniu prototype Costs
            List<ProjectCosts> prototypeCosts = ProjectCostsService.getInstance().getPrototypeCosts();

            //na zaklade danych hodnot vytvori reminders
            result.addAll(createReminders(prototypeCosts));

            //ziskane remindre vlozi do databazy ale len ak sa tam este nenachadzaju
            for (ProjectReminder r : result) {
                if (!old.contains(r.getUnique_code())) {
                    r.insert();
                }

            }

            DbContext.getConnection().commit();

        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            e.printStackTrace();
            //throw new DatabaseException();

        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    private static List<ProjectReminder> createReminders(List<ProjectCosts> data) {
        List<ProjectReminder> reminders = new ArrayList<>();
        for (ProjectCosts elem : data) {
            reminders.add(createReminder(elem));
        }
        return reminders;
    }

    private static ProjectReminder createReminder(ProjectCosts element) {
        String text = "Na projekte sa blíži prekročenie plánovaných " +
                (element.getType() == ProjectCosts.projectCostsType.PROTOTYPE ? "prototypových" : "R&D") +
                " nákladov";
        ProjectReminder reminder = new ProjectReminder();
        reminder.setText(text);
        reminder.setProjectNumber(element.getProjectNumber());
        reminder.setIsClosed(false);
        reminder.setSent(false);
        reminder.setProject_id(element.getProject_id());
        reminder.setUnique_code(element.getProjectNumber() + ":" + element.getActualCosts().toString() + "/" + element.getPlannedCosts().toString());
        return reminder;
    }

    public static void sentReminders() throws SQLException, DatabaseException, GmailMessagingException, ConfigurationException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            List<ProjectReminder> reminders = ProjectReminderService.getInstance().getAllNotSentReminders();

            switch (App.getPropertiesManager().getProperty("reminders.config")) {
                case "ALL_ADMINS":
                    sendProjectAdmins(reminders);
                case "CENTRAL_ADMINS_ONLY":
                    sendCentralAdmins(reminders);
                    break;
                case "MAIN_MAIL":
                    sendToMainMail(reminders);
                    break;
                default:
                    throw new ConfigurationException("Nesprávna hodnota pre konfiguráciu posielania notifikácií");

            }
            //po poslani sa vsetkym remindrom nastavi hodnota sent
            for (ProjectReminder rem : reminders) {
                rem.setSent(true);
                rem.update();
            }

            DbContext.getConnection().commit();

        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            e.printStackTrace();
            //throw new DatabaseException();
        } catch (MessagingException e) {
            DbContext.getConnection().rollback();
            throw new GmailMessagingException();
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    private static void sendMessagge(ProjectReminder rem, String recipient) throws MessagingException {
        String text = "Projekt: " + rem.getProjectNumber();
        SendMail.sendReminder(recipient, text + "\n" + rem.getText());
    }

    private static void sendToMainMail(List<ProjectReminder> reminders) throws MessagingException {
        for (ProjectReminder rem : reminders) {
            sendMessagge(rem, App.getPropertiesManager().getProperty("reminders.mail"));
        }
    }

    private static void sendCentralAdmins(List<ProjectReminder> reminders) throws SQLException, MessagingException {
        //ziska vsetkych centralnych adminov a posle im vsetky remindre
        for (User user : UserService.getInstance().findAllCentralAdmins()) {
            for (ProjectReminder rem : reminders) {
                sendMessagge(rem, user.getEmail());
            }
        }
    }

    private static void sendProjectAdmins(List<ProjectReminder> reminders) throws SQLException, MessagingException {
        for (ProjectReminder rem : reminders) {
            //pre kazdy reminder zisti k akemu projektu sa vztahuje
            // z databazy zoberie len projektovych adminov k tomu projektu
            for (User user : UserService.getInstance().findUsersByProjectID(rem.getProject_id())) {
                sendMessagge(rem, user.getEmail());
            }
        }
    }
}
