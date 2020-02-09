package app.gui.administration;

import app.db.Log;
import app.db.ProjectReminder;
import app.gui.MyAlert;
import app.service.LogService;
import app.service.ProjectReminderService;
import javafx.fxml.FXML;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilesAdministrationController {

    /**
     * Export logov - záznamov používania aplikácie
     */
    @FXML
    public void exportLogs(){
        String directoryName = "C:\\logs";
        String fileName = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss'.txt'").format(new Date());


        ArrayList<Log> logs = LogService.getInstance().getAllLogs();

        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }

        File file = new File(directoryName + "/" + fileName);
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for(Log log: logs){
                String logRecord = "[" + log.getTime() + "] [" + log.getUserFirstName() + " " + log.getUserLastName() + "] - " + log.getText() + "\n" ;
                bw.write(logRecord);
            }
            bw.close();
            MyAlert.showSuccess("Logy boli  úspešne uložené do priečinku " + directoryName);
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

    }

    /**
     * Export logov - záznam všetkých reminderov
     */
    @FXML
    public void exportReport() throws SQLException {
        String directoryName = "C:\\report";
        String fileName = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss'.txt'").format(new Date());


        List<ProjectReminder> projectReminders = ProjectReminderService.getInstance().getAllReminders();

        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }
        if(projectReminders.size() > 0) {
            File file = new File(directoryName + "/" + fileName);
            try{
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                for(ProjectReminder projectReminder: projectReminders){
                    String reportRecord = "[" + projectReminder.getDate() + "] [" + projectReminder.getProjectNumber() + " ] - " + projectReminder.getText() + "\n" ;
                    bw.write(reportRecord);
                }
                bw.close();
                MyAlert.showSuccess("Report bol  úspešne uložený do priečinku " + directoryName);
            }
            catch (IOException e){
                MyAlert.showError("Report sa nepodarilo uložiť");
                e.printStackTrace();
                System.exit(-1);
            }
        }
        else {
            MyAlert.showWarning("V systéme nie sú žiadne remindre");
        }

    }
}
