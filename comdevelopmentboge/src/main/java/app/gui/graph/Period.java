package app.gui.graph;

public class Period{
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
