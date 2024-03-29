package app.gui.project;

import app.App;
import app.config.SignedUser;
import app.exporter.CSVExporter;
import app.exporter.PdfExporter;
import app.gui.MyAlert;
import app.gui.graph.ChartRenderer;
import app.service.ProjectAdministrationService;
import app.service.ProjectService;
import app.service.SAPService;
import app.transactions.ProjectEditTransaction;
import com.itextpdf.text.DocumentException;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import app.db.*;
import javafx.util.converter.BigDecimalStringConverter;
import org.jfree.data.io.CSV;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * controller je zodpovedný za vytvorenie scény s detailom projektu, odchytávanie eventov a zmenu dát
 */
public class ProjectController {

    private static ProjectController projectController;
    public static ProjectController getProjectController(){return projectController;}

    Logger logger = Logger.getLogger(ProjectController.class.toString());

    @FXML
    private TableView projectDetailsTable;

    @FXML
    private TableView sapDetailsTable;

    @FXML
    private HBox firstChartGroup;

    @FXML
    private HBox secondChartGroup;

    @FXML
    private HBox thirdChartGroup;

    @FXML
    private HBox singleChart;

    @FXML
    private ImageView saveButton;

    @FXML
    private ImageView editButton;


    //project table columns
    private TableColumn<Project, String> projectNumber;
    private TableColumn<Project, String> customer;
    private TableColumn<Project, String> projectName;
    private TableColumn<Project, String> partNumber;
    private TableColumn<Project, String> ros;
    private TableColumn<Project, String> roce;
    private TableColumn<Project, BigDecimal> volumes;
    private TableColumn<Project, BigDecimal> ddCost;
    private TableColumn<Project, BigDecimal> prototypeCost;

    //editing table changes
    private HashMap<String, String> changes;

    private String projectDef;
    private List<SAP> sapData;

    @FXML
    public void initialize(){
        projectController = this;

        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Project project = (Project) projectDetailsTable.getItems().get(0);
                saveChangesToDatabase(project);
            }
        });

    }

    /**
     * creates whole content - shows/hides components according to user role, creates tables and charts
     * @param projectDef
     * @throws ParseException
     * @throws IOException
     */
    public void displayProjectData(String projectDef) throws ParseException, IOException {

        //if admin or project admin are signed in, show edit images
        List<Integer> projectAdmins = ProjectAdministrationService.getInstance().getProjectAdminIds(projectDef);
        if(SignedUser.getUser().getUserType().equals("CENTRAL_ADMIN") || projectAdmins.contains(SignedUser.getUser().getId())){
            editButton.setVisible(true);
        }else{
            editButton.setVisible(false);
        }
        this.projectDef = projectDef;
        java.sql.Date initialDate = SAPService.getSapService().getFirstRecordsDate(projectDef);
        ProjectFilter.getInstance().setInitialDate(initialDate);

        createProjectTable();
        createSapTable();
        createCharts();

    }

    /**
     * cretes table with sap information
     * @throws ParseException
     */
    private void createSapTable() throws ParseException {
        TableColumn<SAP, String> ProjektDef = new TableColumn<>("ProjektDef");
        ProjektDef.setCellValueFactory(new PropertyValueFactory<SAP, String>("ProjektDef"));

        TableColumn<SAP, String> PSPElement = new TableColumn<>("PSPElement");
        PSPElement.setCellValueFactory(new PropertyValueFactory<SAP, String>("PSPElement"));

        TableColumn<SAP, String> Objektbezeichnung = new TableColumn<>("Objektbezeichnung");
        Objektbezeichnung.setCellValueFactory(new PropertyValueFactory<>("Objektbezeichnung"));

        TableColumn<SAP, String> Kostenart = new TableColumn<>("Kostenart");
        Kostenart.setCellValueFactory(new PropertyValueFactory<>("Kostenart"));

        TableColumn<SAP, String> KostenartenBez = new TableColumn<>("KostenartenBez");
        KostenartenBez.setCellValueFactory(new PropertyValueFactory<>("KostenartenBez"));

        TableColumn<SAP, String> Bezeichnung = new TableColumn<>("Bezeichnung");
        Bezeichnung.setCellValueFactory(new PropertyValueFactory<>("Bezeichnung"));

        TableColumn<SAP, String> Partnerojekt = new TableColumn<>("Partnerobjekt");
        Partnerojekt.setCellValueFactory(new PropertyValueFactory<>("Partnerobjekt"));

        TableColumn<SAP, String> Periode = new TableColumn<>("Periode");
        Periode.setCellValueFactory(new PropertyValueFactory<>("Periode"));

        TableColumn<SAP, String> Jahr = new TableColumn<>("Jahr");
        Jahr.setCellValueFactory(new PropertyValueFactory<>("Jahr"));

        TableColumn<SAP, String> Belegnr = new TableColumn<>("Belegnr");
        Belegnr.setCellValueFactory(new PropertyValueFactory<>("Belegnr"));

        TableColumn<SAP, Date> BuchDatum = new TableColumn<>("BuchDatum");
        BuchDatum.setCellValueFactory(new PropertyValueFactory<>("BuchDatum"));

        TableColumn<SAP, BigDecimal> WertKWahr = new TableColumn<>("WertKWahr");
        WertKWahr.setCellValueFactory(new PropertyValueFactory<>("WertKWahr"));

        TableColumn<SAP, BigDecimal> KWahr = new TableColumn<>("KWahr");
        KWahr.setCellValueFactory(new PropertyValueFactory<>("KWahr"));

        TableColumn<SAP, Double> MengeErf = new TableColumn<>("MengeErf");
        MengeErf.setCellValueFactory(new PropertyValueFactory<>("MengeErf"));

        TableColumn<SAP, String> GME = new TableColumn<>("GME");
        GME.setCellValueFactory(new PropertyValueFactory<>("GME"));

        sapData = SAPService.getSapService().getSapDataInInterval(projectDef,
                ProjectFilter.getInstance().getFrom(),
                ProjectFilter.getInstance().getTo()
                );

        sapDetailsTable.getItems().clear();
        sapDetailsTable.getItems().addAll(sapData);

        sapDetailsTable.getColumns().clear();
        sapDetailsTable.getColumns().addAll(ProjektDef, PSPElement,Objektbezeichnung,Kostenart, KostenartenBez,Bezeichnung, Partnerojekt
        ,Periode, Jahr, Belegnr, BuchDatum, WertKWahr, KWahr, MengeErf, GME );

        sapDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        sapDetailsTable.setFixedCellSize(50);
        sapDetailsTable.prefWidthProperty().bind(App.getScene().widthProperty());
        sapDetailsTable.prefHeight(50);
    }

    /**
     * creates table with information about project
     */
    private void createProjectTable() {

        projectNumber = new TableColumn<>("Project Nr.");
        projectNumber.setCellValueFactory(new PropertyValueFactory<Project, String>("projectNumber"));

        customer = new TableColumn<>("Customer");
        customer.setCellValueFactory(new PropertyValueFactory<Project, String>("customerName"));

        projectName = new TableColumn<>("ProjectName");
        projectName.setCellValueFactory(new PropertyValueFactory<Project, String>("projectName"));

        partNumber = new TableColumn<>("Part Number");
        partNumber.setCellValueFactory(new PropertyValueFactory<Project, String>("partNumber"));

        ros = new TableColumn<>("Ros");
        ros.setCellValueFactory(new PropertyValueFactory<Project, String>("ros"));

        roce = new TableColumn<>("Roce");
        roce.setCellValueFactory(new PropertyValueFactory<Project, String>("roce"));

        volumes = new TableColumn<>("Volumes");
        volumes.setCellValueFactory(new PropertyValueFactory<Project, BigDecimal>("volumes"));

        ddCost = new TableColumn<>("Offered/Planned \n D&D costs");
        ddCost.setCellValueFactory(new PropertyValueFactory<Project, BigDecimal>("ddCost"));

        prototypeCost = new TableColumn<>("Offered/Planned \n prototype costs");
        prototypeCost.setCellValueFactory(new PropertyValueFactory<Project, BigDecimal>("prototypeCost"));


        Project projectData = ProjectService.getProjectService().findProjectByProjectNumber(projectDef);
        projectDetailsTable.getItems().clear();
        projectDetailsTable.getItems().add(projectData);

        projectDetailsTable.getColumns().clear();
        projectDetailsTable.getColumns().addAll(projectNumber,customer, projectName, partNumber, ros, roce, volumes,ddCost, prototypeCost);

        projectDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //fixme cant resize the table height to height of one row
        projectDetailsTable.setFixedCellSize(50);
        projectDetailsTable.prefHeightProperty().bind(projectDetailsTable.fixedCellSizeProperty().multiply(Bindings.size(projectDetailsTable.getItems()).add(1.01)));
        projectDetailsTable.minHeightProperty().bind(projectDetailsTable.prefHeightProperty());
        projectDetailsTable.maxHeightProperty().bind(projectDetailsTable.prefHeightProperty());

        projectDetailsTable.prefWidthProperty().bind(App.getScene().widthProperty());
    }

    /**
     * creates all charts
     * @throws IOException
     */
    public void createCharts() throws IOException {

        ProjectFilter projectFilter = ProjectFilter.getInstance();

        StackPane projectCostsPane = ChartRenderer.createProjectCostsChart(projectDef, projectFilter.getFrom(), projectFilter.getTo());
        StackPane prototypeCostsPane = ChartRenderer.createProjectPrototypeChart(projectDef, projectFilter.getFrom(), projectFilter.getTo());
        StackPane prototypeRevenuesPane = ChartRenderer.createPrototypeRevenuesChart(projectDef, projectFilter.getFrom(), projectFilter.getTo());
        StackPane rdCostsPane = ChartRenderer.createRDCostsChart(projectDef, projectFilter.getFrom(), projectFilter.getTo());
        StackPane rdTimeCostsPane = ChartRenderer.createRDTimeCostsChart(projectDef, projectFilter.getFrom(), projectFilter.getTo());
        StackPane projectSummaryRevenues = ChartRenderer.createSummaryProjectRevenues(projectDef, projectFilter.getFrom(), projectFilter.getTo());
        StackPane projectSummaryCosts = ChartRenderer.createSummaryProjectCosts(projectDef, projectFilter.getFrom(), projectFilter.getTo());

        firstChartGroup.getChildren().clear();
        projectCostsPane.setPadding(new Insets(0, 50, 0, 0));
        firstChartGroup.getChildren().addAll(projectCostsPane, prototypeCostsPane);

        secondChartGroup.getChildren().clear();
        prototypeRevenuesPane.setPadding(new Insets(0, 50, 0, 0));
        secondChartGroup.getChildren().addAll(prototypeRevenuesPane, rdCostsPane);

        singleChart.getChildren().clear();
        singleChart.getChildren().add(rdTimeCostsPane);

        thirdChartGroup.getChildren().clear();
        thirdChartGroup.getChildren().addAll(projectSummaryRevenues, projectSummaryCosts);
    }

    /**
     * handles events from editing table with project information
     */
    @FXML
    private void setEditableMode(){
        projectDetailsTable.setEditable(true);
        saveButton.setVisible(true);
        changes = new HashMap<>();

        projectName.setOnEditCommit(
                (TableColumn.CellEditEvent<Project, String> t) ->
                {( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setProjectName(t.getNewValue());
                  changes.put("projectName", t.getNewValue());
                }
        );

        projectName.setCellFactory(
                TextFieldTableCell.forTableColumn());

        partNumber.setOnEditCommit(
                (TableColumn.CellEditEvent<Project, String> t) ->
                {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPartNumber(t.getNewValue());
                    changes.put("partNumber", t.getNewValue());
                }
        );

        partNumber.setCellFactory(
                TextFieldTableCell.forTableColumn());
        customer.setOnEditCommit(
                (TableColumn.CellEditEvent<Project, String> t) ->
                {     ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setCustomerName(t.getNewValue());
                    changes.put("customer", t.getNewValue());
                }
        );

        customer.setCellFactory(
                TextFieldTableCell.forTableColumn());

        projectNumber.setOnEditCommit(
                (TableColumn.CellEditEvent<Project, String> t) ->
                {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setProjectNumber(t.getNewValue());
                    changes.put("projectNumber", t.getNewValue());
                }
        );

        projectNumber.setCellFactory(
                TextFieldTableCell.forTableColumn());

        roce.setOnEditCommit(
                (TableColumn.CellEditEvent<Project, String> t) ->
                {       ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setRoce(t.getNewValue());
                    changes.put("roce", t.getNewValue());
                }
        );

        roce.setCellFactory(
                TextFieldTableCell.forTableColumn());
        ros.setOnEditCommit(
                (TableColumn.CellEditEvent<Project, String> t) ->
                {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setRos(t.getNewValue());
                    changes.put("ros", t.getNewValue());
                }

        );

        ros.setCellFactory(
                TextFieldTableCell.forTableColumn());
        ddCost.setOnEditCommit(
                (TableColumn.CellEditEvent<Project, BigDecimal> t) ->
                {      ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDdCost(t.getNewValue());
                    changes.put("D&D Costs", String.valueOf(t.getNewValue()));
                }
        );

        ddCost.setCellFactory(
                TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        prototypeCost.setOnEditCommit(
                (TableColumn.CellEditEvent<Project, BigDecimal> t) ->
                {       ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPrototypeCost(t.getNewValue());
                    changes.put("Prototype Costs", String.valueOf(t.getNewValue()));
                }
        );

        prototypeCost.setCellFactory(
                TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        volumes.setOnEditCommit(
                (TableColumn.CellEditEvent<Project, BigDecimal> t) ->
                {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setVolumes(t.getNewValue());
                    changes.put("volumes", String.valueOf(t.getNewValue()));
                }
        );

        volumes.setCellFactory(
                TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
    }

    /**
     * calls database transaction to save changes into database
     * @param project
     */
    @FXML
    public void saveChangesToDatabase(Project project){
        saveButton.setVisible(false);
        try {
            ProjectEditTransaction.editProject(project, changes);
            createCharts();
        } catch (SQLException e) {
            MyAlert.showError("Údaje o projekte "+project.getProjectNumber() + " sa nepodarilo zmeniť!");
            e.printStackTrace();
        } catch (IOException e) {
            MyAlert.showError("Vyskytla sa chyba pri kreslení grafov.");
            e.printStackTrace();
        }
    }

    /**
     * filters SAP data according to values from datepickers from and to
     * @param from
     * @param to
     * @throws ParseException
     */
    public void filterSapData(java.sql.Date from, java.sql.Date to) throws ParseException {
        List<SAP> sapData = SAPService.getSapService().getSapDataInInterval(projectDef, from, to);
        sapDetailsTable.getItems().clear();
        sapDetailsTable.getItems().addAll(sapData);
    }

    /**
     * exports all content into pdf
     * @throws IOException
     * @throws DocumentException
     */
    @FXML
    public void exportProjectToPDf() throws IOException{
        try {
            PdfExporter.exportPdf(projectDetailsTable.getItems(), sapDetailsTable.getItems());
        } catch (DocumentException e) {
            MyAlert.showError("Problém pri exportovaní PDF.");
            e.printStackTrace();
        }
    }

    /**
     * exports project data to csv format file
     */
    @FXML
    public void exportProjectDataToCSV(){
        Project project = ProjectService.getProjectService().findProjectByNum(projectDef);
        List<String> values = new ArrayList<>();
        //should not happen, but...
        if(project!=null){
            values = project.getAllAttributesValues();
        }
        CSVExporter.exportDataToCSV(List.of(values), "project");
    }

    /**
     * exports sap data to csv format file
     */
    @FXML
    public void exportSAPDataToCSV(){
        List<List<String>> values = new ArrayList<>();
        if(sapData != null){
            for(SAP sap: sapData){
                values.add(sap.getAllAttributesValues());
            }
        }

        CSVExporter.exportDataToCSV(values, "sap");
    }
}
