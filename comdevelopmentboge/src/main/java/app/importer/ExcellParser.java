package app.importer;

import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class ExcellParser {

    /**
     * Precitanie zadaneho suboru a vlozenie riadku do pola
     * @throws IOException
     * @param inputFile - subor, ktory sa ma spracovat
     */
    public static ArrayList<ExcelRow> readFromFile(File inputFile) throws IOException {
        FileInputStream file = null;
        if(inputFile == null){
            String path = ExcellParser.class.getResource("SAP_DATA_ver2.xlsx").getPath();
            file = new FileInputStream(new File(path));
        }
        else{
            file = new FileInputStream(inputFile);
        }

        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file);

        }catch (NotOfficeXmlFileException e){
            e.printStackTrace();
            throw new IOException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        ArrayList<ExcelRow> list = new ArrayList();
        int colCounter = 0;
        int rowCounter = 0;
        java.util.Date date = new Date(0);
        for (Row row : sheet) {
            ExcelRow excelRow = new ExcelRow();
            colCounter=0;
            if(rowCounter == 0){
                rowCounter++;
                continue;
            }
            ArrayList<String> temp = new ArrayList();
            for (Cell cell : row) {

                if(colCounter>15){
                    break;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        System.out.println(cell.getStringCellValue());
                        temp.add(cell.getStringCellValue());
                        if (cell.getStringCellValue().equals(" ")) {
                            temp.add("");
                        }
                        break;

                    case BLANK:

                        temp.add("");
                        break;

                    case NUMERIC:
                        if (colCounter == 10) {
                            date = cell.getDateCellValue();
                        }

                        temp.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                }
                colCounter++;
            }

            excelRow.setData(temp);
            excelRow.setDate(date);
            list.add(excelRow);

        }


        return list;
    }




}
