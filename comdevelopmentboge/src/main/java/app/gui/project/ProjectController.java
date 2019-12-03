package app.gui.project;

import app.App;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.List;

public class ProjectController {

    @FXML
    private TableView projectDetailsTable;

    @FXML
    private GridPane projectGraphGrid;

    @FXML
    public void initialize() {

        projectDetailsTable.setEditable(true);

        TableColumn<Project, String> projectNumber = new TableColumn<>("Project Nr.");
        projectNumber.setCellValueFactory(new PropertyValueFactory<Project, String>("projectNumber"));

        TableColumn<Project, String> customer = new TableColumn<>("Customer");
        customer.setCellValueFactory(new PropertyValueFactory<Project, String>("customer"));

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
        Project project = new Project();
        project.setProjectNumber("CH-255454");
        project.setCustomer("Audi");
        project.setProjectName("Project1");

        // projectDetailsTable.getItems().clear();
        projectDetailsTable.getItems().add(project);


        projectDetailsTable.getColumns().clear();
        projectDetailsTable.getColumns().addAll(projectNumber,customer, projectName, partNumber, ros, roce, volumes,ddCost, prototypeCost);

        projectDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        projectDetailsTable.setFixedCellSize(50);
        projectDetailsTable.prefHeightProperty().bind(projectDetailsTable.fixedCellSizeProperty().multiply(Bindings.size(projectDetailsTable.getItems()).add(1.01)));
        projectDetailsTable.minHeightProperty().bind(projectDetailsTable.prefHeightProperty());
        projectDetailsTable.maxHeightProperty().bind(projectDetailsTable.prefHeightProperty());

        projectDetailsTable.prefWidthProperty().bind(App.getScene().widthProperty());

        BorderPane pane = new BorderPane();
       // pane.setCenter(GraphRenderer.getGraph());
        projectGraphGrid.add(pane, 0, 0);
    }

}
