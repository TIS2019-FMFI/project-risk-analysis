package app.db;


public class FEMReminder extends Reminder {

    private String type = "FEM simulácie";

    public FEMReminder() {}

    public FEMReminder(Integer id, String text) {
        super(id, text);
        super.setReminderType(REMINDERTYPE.FEM);
    }

    public String getType() { return type; }

}
