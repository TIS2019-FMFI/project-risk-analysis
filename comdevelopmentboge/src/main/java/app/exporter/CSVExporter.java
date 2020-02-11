package app.exporter;

import app.App;
import app.gui.MyAlert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CSVExporter {

    public static void exportDataToCSV(List<List<String>> data, String fileIdentifier){
        String directoryName = App.getPropertiesManager().getProperty("file.location");
        if(directoryName == null || "".equals(directoryName)){
            MyAlert.showWarning("Nenastavili ste priečinok na export csv suboru,\n subor bol exportovaný do priečinka C:/file");
            directoryName="C:/file";
        }
        String fileName = fileIdentifier + new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss'.txt'").format(new Date());

        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }

        File file = new File(directoryName + "/" + fileName);
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for(List<String> sample: data){
                for(int i=0; i< sample.size(); i++){
                    bw.write(sample.get(i));
                    if(i != sample.size()-1){
                        bw.write(",");
                    } else{
                        bw.write("\n");
                    }
                }
            }
            bw.close();
            MyAlert.showSuccess("Súbor bol úspešne uložený do priečinku " + directoryName);

        } catch (IOException e) {
            MyAlert.showError("Súbor sa nepodarilo uložiť");
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
