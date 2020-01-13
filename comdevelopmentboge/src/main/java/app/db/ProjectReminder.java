package app.db;

import java.util.Date;

public class ProjectReminder extends Reminder {

    private String projectNumber;

    public ProjectReminder(Integer id, String text, Integer partNumber, Date timePeriod) {
        super(id, text, partNumber, timePeriod);
        super.setReminderType(REMINDERTYPE.PROJECT);
    }

    public ProjectReminder(Integer id, String text, String projectNumber) {
        super(id, text);
        super.setReminderType(REMINDERTYPE.PROJECT);
        this.projectNumber = projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

}
