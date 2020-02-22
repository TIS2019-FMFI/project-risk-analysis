package app.gui.project;

import app.gui.TabController;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;

public class ProjectFilter {

    private static ProjectFilter projectFilter;
    public static ProjectFilter getInstance(){return projectFilter;};

    @FXML
    private DatePicker fromDatepicker;
    @FXML
    private DatePicker toDatepicker;

    private java.sql.Date from;
    private java.sql.Date to;

    private java.sql.Date initialDate;

    @FXML
    public void initialize(){
        projectFilter = this;



        fromDatepicker.setOnAction(event -> {
            LocalDate date = fromDatepicker.getValue();
            from = ((date==null)?null:Date.valueOf(date));
            try {
                filterData();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        toDatepicker.setOnAction(event -> {
            LocalDate date = toDatepicker.getValue();
            to = ((date==null)?null:Date.valueOf(date));
            try {
                filterData();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * handles pressing back button
     * @throws IOException
     */
    @FXML
    public void onBackPressed() throws IOException {
        ProjectListFilter.getProjectListFilter().restartValues();
        TabController.getInstance().selectProjectTab();
    }

    /**
     * filters project data according to datepickers values
     * @throws ParseException
     * @throws IOException
     */
    private void filterData() throws ParseException, IOException {
        ProjectController.getProjectController().filterSapData((from!=null)?from:initialDate, (to!=null)?to:(new Date(System.currentTimeMillis())));
        ProjectController.getProjectController().createCharts();

    }

    /**
     * refreshes filter after pressing refresh button
     * @throws ParseException
     * @throws IOException
     */
    @FXML
    public void refreshFilter() throws ParseException, IOException {
        from = null;
        to = null;

        fromDatepicker.setValue(null);
        toDatepicker.setValue(null);
        filterData();
        ProjectController.getProjectController().createCharts();
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }


    public Date getFrom() {
        return (from==null)?initialDate:from;
    }

    public Date getTo() {
        return (to==null)?(new java.sql.Date(System.currentTimeMillis())):to;
    }
}
