package app.gui.project;

import app.App;
import app.config.SignedUser;
import app.exception.DatabaseException;
import app.gui.graph.ChartRenderer;
import app.gui.graph.Period;
import app.service.AdministrationService;
import app.service.CustomerService;
import app.service.ProjectService;
import app.service.SAPService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import app.db.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.jfree.chart.ChartPanel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

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
    private ImageView saveButton;
    @FXML
    private ImageView editButton;
    @FXML
    private FlowPane editPane;

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

    public void displayProjectData(String projectDef) throws ParseException, IOException {

        //if admin or project admin are signed in, show edit images
        List<Integer> projectAdmins = AdministrationService.getInstance().getProjectAdminIds(projectDef);
        if(SignedUser.getUser().getUserType().equals("ADMIN") || projectAdmins.contains(SignedUser.getUser().getId())){
            editPane.setVisible(true);
        }else{
            editPane.setVisible(false);
        }

        createProjectTable(projectDef);
        createSapTable(projectDef);
        createCharts(projectDef);
    }

    private void createSapTable(String projectDef) throws ParseException {
        TableColumn<SAP, String> PSPElement = new TableColumn<>("PSPElement");
        PSPElement.setCellValueFactory(new PropertyValueFactory<SAP, String>("PSPElement"));

        TableColumn<SAP, String> Objektbezeichnung = new TableColumn<>("Objektbezeichnung");
        Objektbezeichnung.setCellValueFactory(new PropertyValueFactory<>("Objektbezeichnung"));

        TableColumn<SAP, String> Kostenart = new TableColumn<>("Kostenart");
        Kostenart.setCellValueFactory(new PropertyValueFactory<>("Kostenart"));

        TableColumn<SAP, String> KostenartenBez = new TableColumn<>("KostenartenBez");
        KostenartenBez.setCellValueFactory(new PropertyValueFactory<>("KostenartenBez"));

        TableColumn<SAP, String> Partnerojekt = new TableColumn<>("Partnerojekt");
        Partnerojekt.setCellValueFactory(new PropertyValueFactory<>("Partnerojekt"));

        TableColumn<SAP, String> Periode = new TableColumn<>("Periode");
        Periode.setCellValueFactory(new PropertyValueFactory<>("Periode"));

        TableColumn<SAP, String> Jahr = new TableColumn<>("Jahr");
        Jahr.setCellValueFactory(new PropertyValueFactory<>("Jahr"));

        TableColumn<SAP, Date> BuchDatum = new TableColumn<>("BuchDatum");
        BuchDatum.setCellValueFactory(new PropertyValueFactory<>("BuchDatum"));

        TableColumn<SAP, BigDecimal> Wert = new TableColumn<>("Wert");
        Wert.setCellValueFactory(new PropertyValueFactory<>("Wert"));

        TableColumn<SAP, Double> Menge = new TableColumn<>("Menge");
        Menge.setCellValueFactory(new PropertyValueFactory<>("Menge"));

        TableColumn<SAP, String> GME = new TableColumn<>("GME");
        GME.setCellValueFactory(new PropertyValueFactory<>("GME"));

        List<SAP> data = SAPService.getSapService().getSapData(projectDef);

        sapDetailsTable.getItems().clear();
        sapDetailsTable.getItems().addAll(data);

        sapDetailsTable.getColumns().clear();
        sapDetailsTable.getColumns().addAll(PSPElement,Objektbezeichnung,Kostenart, KostenartenBez,Partnerojekt
        ,Periode, Jahr, BuchDatum, Wert, Menge, GME );

        sapDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        sapDetailsTable.setFixedCellSize(50);
        sapDetailsTable.prefWidthProperty().bind(App.getScene().widthProperty());
        sapDetailsTable.prefHeight(50);
    }

    private void createProjectTable(String projectDef) {

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

    private void createCharts(String projectDef) throws IOException {

        StackPane projectCostsPane = ChartRenderer.createProjectCostsChart(projectDef);
        StackPane prototypeCostsPane = ChartRenderer.createProjectPrototypeChart(projectDef);
        StackPane prototypeRevenuesPane = ChartRenderer.createPrototypeRevenuesChart(projectDef);
        StackPane rdCostsPane = ChartRenderer.createRDCostsChart(projectDef);
        StackPane projectSummaryRevenues = ChartRenderer.createSummaryProjectRevenues(projectDef);
        StackPane projectSummaryCosts = ChartRenderer.createSummaryProjectCosts(projectDef);

        firstChartGroup.getChildren().clear();
        firstChartGroup.getChildren().addAll(projectCostsPane, prototypeCostsPane);
        secondChartGroup.getChildren().clear();
        secondChartGroup.getChildren().addAll(prototypeRevenuesPane, rdCostsPane);
        thirdChartGroup.getChildren().addAll(projectSummaryRevenues, projectSummaryCosts);
    }

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

    @FXML
    public void saveChangesToDatabase(Project project){

        Customer customer = null;
        if(project.getCustomerName()!=null){
            try {
                customer = CustomerService.getCustomerService().findCustomerByName(project.getCustomerName());
            } catch (DatabaseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    alert.close();
                    return;
                }
            }
        }

        saveButton.setVisible(false);

        if(customer!=null){
            project.setCustomerId(customer.getId());
            project.update();
        }
        logChanges(project.getProjectNumber());

    }

    private void logChanges(String projectDef){
        Integer userId = SignedUser.getUser().getId();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String text = "updated values:";

        for(String columnName: changes.keySet()){
            text += columnName + "=" + changes.get(columnName) + ", ";
        }

        text = text.substring(0, text.length() - 2) + " on project: " + projectDef;

        Log log = new Log();
        log.setUserId(userId);
        log.setTime(currentTime);
        log.setText(text);
        log.insert();
    }
}
