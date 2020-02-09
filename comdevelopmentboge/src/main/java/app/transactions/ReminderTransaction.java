package app.transactions;

import app.config.DbContext;
import app.config.SendMail;
import app.config.SignedUser;
import app.db.ProjectCosts;
import app.db.ProjectReminder;
import app.db.User;
import app.exception.DatabaseException;
import app.exception.GmailMessagingException;
import app.service.ProjectCostsService;
import app.service.ProjectReminderService;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReminderTransaction {
    public static void loadReminders() throws SQLException, DatabaseException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            List<ProjectReminder> result = new ArrayList<>();

            //najde projekty, ktore sa blizia k prekroceniu DDCosts
            List<ProjectCosts> DDCosts = ProjectCostsService.getInstance().getDDCosts();

            //na zaklade danych hodnot vytvori reminders
            result.addAll(createReminders(DDCosts));

            //najde projekty, ktore sa blizia k prekroceniu prototype Costs
            List<ProjectCosts> prototypeCosts = ProjectCostsService.getInstance().getPrototypeCosts();

            //na zaklade danych hodnot vytvori reminders
            result.addAll(createReminders(prototypeCosts));

            //ziskane remindre vlozi do databazy
            for(ProjectReminder r : result) {
                r.insert();
            }

        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            throw new DatabaseException();

        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }
    private static List<ProjectReminder> createReminders(List<ProjectCosts> data) {
        List<ProjectReminder> reminders = new ArrayList<>();
        for(ProjectCosts elem : data) {
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
        reminder.setUnique_code(element.getActualCosts().toString()+"/"+element.getPlannedCosts().toString());
        return reminder;
    }
    public static void sentReminders() throws SQLException, DatabaseException, GmailMessagingException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            List<ProjectReminder> reminders = ProjectReminderService.getInstance().getAllNotSentReminders();

            for (ProjectReminder rem : reminders) {
                String text = "Projekt: " + rem.getProjectNumber();
                SendMail.sendReminder("linda.jurkasova@gmail.com",text + "\n" + rem.getText());
                rem.setSent(true);
                rem.update();
            }

        } catch (SQLException  e) {
            DbContext.getConnection().rollback();
            throw new DatabaseException();
        } catch (MessagingException e) {
            throw new GmailMessagingException();
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }
}
