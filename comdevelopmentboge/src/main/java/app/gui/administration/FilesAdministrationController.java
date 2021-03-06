package app.gui.administration;

import app.App;
import app.db.Log;
import app.db.ProjectReminder;
import app.gui.MyAlert;
import app.importer.ExcelRow;
import app.importer.ExcellParser;
import app.service.LogService;
import app.service.ProjectReminderService;
import app.transactions.ImportSAPTransaction;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

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
     * Export logov - zaznamov pouzivania aplikacie
     */
    @FXML
    public void exportLogs(){
        String directoryName = "";

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File folder = directoryChooser.showDialog(null);
        if(folder != null){
            directoryName = folder.getAbsolutePath();
        } else{
            return;
        }


        if(directoryName == null || "".equals(directoryName)){
            MyAlert.showWarning("Nenastavili ste priečinok na export logov,\n logy boli exportované do priečinka C:/logs");
            directoryName="C:/logs";
        }
        String fileName = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss'.txt'").format(new Date());


        ArrayList<Log> logs = LogService.getInstance().getAllLogs();

        if(logs.size()>0){
            File directory = new File(directoryName);
            if (! directory.exists()){
                directory.mkdir();
            }

            File file = new File(directoryName + "/" + fileName);
            try{
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                for(Log log: logs){
                    String logRecord = "[" + log.getTime() + "] [" + log.getUserFirstName() + " " + log.getUserLastName() + "] - " + log.getText() + "\n";
                    bw.write(logRecord);
                }
                bw.close();
                MyAlert.showSuccess("Logy boli úspešne uložené do priečinku\n" + directoryName);
            }
            catch (IOException e){
                MyAlert.showError("Logy sa nepodarilo uložiť");
                e.printStackTrace();
                System.exit(-1);
            }
        } else{
            MyAlert.showWarning("V systéme sa nenachádzajú žiadne logy.");
        }
    }

    /**
     * Export logov - zaznam vsetkych reminderov
     */
    @FXML
    public void exportReport() throws SQLException {
        String directoryName = "";

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File folder = directoryChooser.showDialog(null);
        if(folder != null){
            directoryName = folder.getAbsolutePath();
        }else{
            return;
        }


        if(directoryName == null || "".equals(directoryName)){
            MyAlert.showWarning("Nenastavili ste priečinok na export reportu,\n report bol exportovaný do priečinka C:/reports");
            directoryName="C:/report";
        }
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
                    String actual = String.format("%.2f", Double.parseDouble(projectReminder.getUnique_code().substring(projectReminder.getUnique_code().indexOf(":")+1,projectReminder.getUnique_code().indexOf("/"))));
                    String planned = String.format("%.2f", Double.parseDouble(projectReminder.getUnique_code().substring(projectReminder.getUnique_code().indexOf("/")+1)));
                    String reportRecord = "[" + projectReminder.getDate() + "] [" + projectReminder.getProjectNumber() + " ] - " +
                            projectReminder.getText() + " [Aktualne = "+ actual + "] [Planovane = "+ planned + "]\n" ;
                    bw.write(reportRecord);
                }
                bw.close();
                MyAlert.showSuccess("Report bol  úspešne uložený do priečinku\n" + directoryName);
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


    /**
     * Spusti vyber suboru na import
     */
    @FXML
    private void clickImport() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile == null){
            return;
        }
        ArrayList<ExcelRow> sap = null;
        try {
            sap = ExcellParser.readFromFile(selectedFile);
        } catch (IOException e) {
            MyAlert.showError("Súbor sa nepodarilo načítať.");
            e.printStackTrace();
            return;
        }
        if(sap != null){

            try {
                ImportSAPTransaction.importSAP(sap);
            } catch (SQLException e) {
                MyAlert.showError("Spojenie s databázou nebolo úspešné.");
                e.printStackTrace();
                return;
            }
            MyAlert.showSuccess("Súbor úspešne načítaný.");
        }

    }
}
