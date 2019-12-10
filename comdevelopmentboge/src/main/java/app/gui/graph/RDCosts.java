package app.gui.graph;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class RDCosts {

    private static LinkedHashMap<Period, BigDecimal> costs = new LinkedHashMap<>();

    public static LinkedHashMap<Period, BigDecimal> getCosts(){return costs;}

    public void addRecord(String month, String year, BigDecimal amount) {
        costs.put(new Period(month, year), amount);
    }



    class Period{
        private String month;
        private String year;

        public Period(String month, String year) {
            this.month = month;
            this.year = year;
        }


        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String toString(){
            return month + "/" + year;
        }
    }
}
