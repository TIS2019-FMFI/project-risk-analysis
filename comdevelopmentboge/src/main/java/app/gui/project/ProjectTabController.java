package app.gui.project;

import app.App;
import app.gui.TabController;
import app.service.ProjectService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import com.jfoenix.controls.*;

import java.io.IOException;
import java.util.ArrayList;

public class ProjectTabController{

    private static ProjectTabController instance;
    public static ProjectTabController getInstance(){return instance;}

    @FXML
    private JFXListView<Label> projectListView;

    @FXML
    public void initialize() {

        instance = this;

        ArrayList<String> projectNumbers = ProjectService.getProjectService().getAllProjectNumbers();
        for(String number : projectNumbers) {
            Label label = new Label();
            label.setId(number);
            label.setText(number);
            projectListView.getItems().add(label);
        }

        projectListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Label label = projectListView.getSelectionModel().getSelectedItem();
                if(label != null) {
                    try {
                        showProjectDetails(label.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        projectListView.setPrefWidth(App.getScene().getWidth());
        projectListView.setPrefHeight(App.getScene().getHeight() - 80);
    }

    private void showProjectDetails(String projectDef) throws IOException {
        TabController.getInstance().selectProjectDetailsTab(projectDef);
    }


    public void reloadList(){
        ArrayList<String> projectNumbers = ProjectService.getProjectService().findProjectsByCriteria();
        projectListView.getItems().clear();
        for(String number : projectNumbers) {
            Label label = new Label();
            label.setId(number);
            label.setText(number);
            projectListView.getItems().add(label);
            System.out.println(number);
        }
    }
}
