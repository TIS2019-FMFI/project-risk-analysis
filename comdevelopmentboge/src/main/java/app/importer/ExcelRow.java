package app.importer;

import java.util.ArrayList;

public class ExcelRow {
    /**
     * Textove data jedneho riadku
     */
    ArrayList<String> data;
    /**
     * Datum ulozeny v riadku
     */
    java.util.Date date;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }
}
