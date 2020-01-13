package app.gui.project;

import app.App;
import app.gui.TabController;
import app.gui.graph.ChartRenderer;
import app.service.SAPService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import app.db.*;
import javafx.scene.layout.VBox;
import org.apache.poi.ss.usermodel.Table;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ProjectController {

    Logger logger = Logger.getLogger(ProjectController.class.toString());

    @FXML
    private TableView projectDetailsTable;

    @FXML
    private TableView sapDetailsTable;

    @FXML
    private GridPane projectGraphGrid;


    @FXML
    public void initialize() throws ClassNotFoundException, IOException, ParseException {
        logger.info("project page initialization");

        createProjectTable();
        createSapTable();
        createCharts();
    }

    private void createSapTable() throws ParseException {
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

        List<SAP> data = SAPService.getSapService().getAllSAPData();

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

    private void createProjectTable() {
        projectDetailsTable.setEditable(true);

        TableColumn<Projects, String> projectNumber = new TableColumn<>("Project Nr.");
        projectNumber.setCellValueFactory(new PropertyValueFactory<Projects, String>("projectNumber"));

        TableColumn<Projects, String> customer = new TableColumn<>("Customer");
        customer.setCellValueFactory(new PropertyValueFactory<Projects, String>("customer"));

        TableColumn<Projects, String> projectName = new TableColumn<>("ProjectName");
        projectName.setCellValueFactory(new PropertyValueFactory<Projects, String>("projectName"));

        TableColumn<Projects, String> partNumber = new TableColumn<>("Part Number");
        partNumber.setCellValueFactory(new PropertyValueFactory<Projects, String>("partNumber"));

        TableColumn<Projects, String> ros = new TableColumn<>("Ros");
        ros.setCellValueFactory(new PropertyValueFactory<Projects, String>("ros"));

        TableColumn<Projects, String> roce = new TableColumn<>("Roce");
        roce.setCellValueFactory(new PropertyValueFactory<Projects, String>("roce"));

        TableColumn<Projects, Integer> volumes = new TableColumn<>("Volumes");
        volumes.setCellValueFactory(new PropertyValueFactory<Projects, Integer>("volumes"));

        TableColumn<Projects, Integer> ddCost = new TableColumn<>("Offered/Planned \n D&D costs");
        ddCost.setCellValueFactory(new PropertyValueFactory<Projects, Integer>("ddCost"));

        TableColumn<Projects, Integer> prototypeCost = new TableColumn<>("Offered/Planned \n prototype costs");
        prototypeCost.setCellValueFactory(new PropertyValueFactory<Projects, Integer>("prototypeCost"));


        projectDetailsTable.getColumns().clear();
        projectDetailsTable.getColumns().addAll(projectNumber,customer, projectName, partNumber, ros, roce, volumes,ddCost, prototypeCost);

        projectDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        projectDetailsTable.setFixedCellSize(50);
        projectDetailsTable.prefHeightProperty().bind(projectDetailsTable.fixedCellSizeProperty().multiply(Bindings.size(projectDetailsTable.getItems()).add(1.01)));

        projectDetailsTable.prefWidthProperty().bind(App.getScene().widthProperty());
    }

    private void createCharts() throws IOException {
        BorderPane rdCostsPane = new BorderPane();
        BorderPane projectCostsPane = new BorderPane();
        BorderPane prototypeCostsPane= new BorderPane();
        BorderPane prototypeRevenuesPane= new BorderPane();

        SwingNode rdCostsNode = new SwingNode();
        SwingNode projectCostsNode = new SwingNode();
        SwingNode prototypeCostsNode = new SwingNode();
        SwingNode prototypeRevenuesNode = new SwingNode();

        ChartPanel rdCostsChart = ChartRenderer.createRDCostsChart("CH-060968");
        ChartPanel projectCostsChart = ChartRenderer.createProjectCostsChart("CH-060968");
        ChartPanel prototypeCostsChart = ChartRenderer.createProjectPrototypeChart("CH-060020");
        ChartPanel prototypeRevenuesChart = ChartRenderer.createPrototypeRevenuesChart("PC-060890");

        createSwingContent(rdCostsNode, rdCostsChart);
        createSwingContent(projectCostsNode, projectCostsChart);
        createSwingContent(prototypeCostsNode, prototypeCostsChart);
        createSwingContent(prototypeRevenuesNode, prototypeRevenuesChart);

        rdCostsPane.setCenter(rdCostsNode);
        projectCostsPane.setCenter(projectCostsNode);
        prototypeCostsPane.setCenter(prototypeCostsNode);
        prototypeRevenuesPane.setCenter(prototypeRevenuesNode);

        projectGraphGrid.add(rdCostsPane, 0, 0);
        projectGraphGrid.add(projectCostsPane, 1,0);
        projectGraphGrid.add(prototypeCostsPane, 0,1);
        projectGraphGrid.add(prototypeRevenuesNode, 1,1);
    }

    private void createSwingContent(final SwingNode swingNode, final ChartPanel panel) throws IOException {

        Platform.runLater(()->{
            swingNode.setContent(panel);

        });
    }

}
