package app.gui.administration;

import app.db.Log;
import app.service.LogService;
import javafx.fxml.FXML;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
