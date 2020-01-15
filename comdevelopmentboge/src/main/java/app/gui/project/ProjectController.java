package app.gui.project;

import app.App;
import app.gui.graph.ChartRenderer;
import app.gui.graph.Period;
import app.service.ProjectService;
import app.service.SAPService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import app.db.*;
import org.jfree.chart.ChartPanel;

import java.math.BigDecimal;
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
    public void initialize(){
        projectController = this;
    }

    public void displayProjectData(String projectDef) throws ParseException, IOException {

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
        projectDetailsTable.setEditable(true);

        TableColumn<Project, String> projectNumber = new TableColumn<>("Project Nr.");
        projectNumber.setCellValueFactory(new PropertyValueFactory<Project, String>("projectNumber"));

        TableColumn<Project, String> customer = new TableColumn<>("Customer");
        customer.setCellValueFactory(new PropertyValueFactory<Project, String>("customerName"));

        TableColumn<Project, String> projectName = new TableColumn<>("ProjectName");
        projectName.setCellValueFactory(new PropertyValueFactory<Project, String>("projectName"));

        TableColumn<Project, String> partNumber = new TableColumn<>("Part Number");
        partNumber.setCellValueFactory(new PropertyValueFactory<Project, String>("partNumber"));

        TableColumn<Project, String> ros = new TableColumn<>("Ros");
        ros.setCellValueFactory(new PropertyValueFactory<Project, String>("ros"));

        TableColumn<Project, String> roce = new TableColumn<>("Roce");
        roce.setCellValueFactory(new PropertyValueFactory<Project, String>("roce"));

        TableColumn<Project, Integer> volumes = new TableColumn<>("Volumes");
        volumes.setCellValueFactory(new PropertyValueFactory<Project, Integer>("volumes"));

        TableColumn<Project, Integer> ddCost = new TableColumn<>("Offered/Planned \n D&D costs");
        ddCost.setCellValueFactory(new PropertyValueFactory<Project, Integer>("ddCost"));

        TableColumn<Project, Integer> prototypeCost = new TableColumn<>("Offered/Planned \n prototype costs");
        prototypeCost.setCellValueFactory(new PropertyValueFactory<Project, Integer>("prototypeCost"));

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
}
