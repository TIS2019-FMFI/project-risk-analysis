package app.importer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcellParser {

    public static ArrayList<ArrayList<String>> readFromFile() throws IOException {
        String path = "./resources/SAP_DATA_ver2.xlsx";

        FileInputStream file = new FileInputStream(new File(path));
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);
        ArrayList<ArrayList<String>> list = new ArrayList();

        for(Row row: sheet){
            ArrayList<String> temp = new ArrayList();
            for(Cell cell : row){
                switch (cell.getCellType()) {
                    case STRING:
                        temp.add(cell.getStringCellValue());
                        break;
                        /*
                    case BOOLEAN:
                        temp.add(cell.getBooleanCellValue());
                        break;
                        */
                    case NUMERIC:
                        temp.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                }
            }
            list.add(temp);


        }


        return list;
    }
}
