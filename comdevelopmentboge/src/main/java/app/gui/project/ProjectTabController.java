package app.gui.project;

import app.App;
import app.gui.TabController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;


import java.io.IOException;
import java.util.logging.Logger;

public class ProjectTabController extends TabController{

    Logger logger = Logger.getLogger(ProjectTabController.class.toString());

    private static ProjectTabController instance;
    public static ProjectTabController getInstance(){
        if(instance == null){
            instance = new ProjectTabController();
        }
        return instance;
    }

    @FXML
    private ScrollPane projectListBoard;

    @FXML
    public void initialize()  {
        ObservableList<ListItem> data = FXCollections.observableArrayList();
        data.addAll(new ListItem("Projekt1"), new ListItem("Projekt2"));

        final ListView<ListItem> listView = new ListView<>(data);
        listView.setCellFactory(new Callback<ListView<ListItem>, ListCell<ListItem>>() {
            @Override
            public ListCell<ListItem> call(ListView<ListItem> listView) {
                return new CustomListCell();
            }
        });

        projectListBoard.prefWidthProperty().bind(App.getScene().widthProperty());
        projectListBoard.prefHeightProperty().bind(App.getScene().heightProperty());

        listView.prefHeightProperty().bind(projectListBoard.prefHeightProperty());
        listView.prefWidthProperty().bind(projectListBoard.prefWidthProperty());

        projectListBoard.setContent(listView);
        projectListBoard.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ListItem item =  listView.getSelectionModel().getSelectedItem();
                try {
                    showProjectDetails(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showProjectDetails(ListItem item) throws IOException {
        projectListBoard.setContent(FXMLLoader.load(ProjectController.class.getResource("project-details-board.fxml")));
    }

    private static class ListItem {
        private String name;

        public String getName() {
            return name;
        }

        public ListItem(String name) {
            super();
            this.name = name;
        }
    }

    private class CustomListCell extends ListCell<ListItem> {
        private HBox content;
        private Text name;

        public CustomListCell() {
            super();
            name = new Text();
            content = new HBox(name);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(ListItem item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                content.setStyle("-fx-background-color: grey");
                content.setPrefHeight(30);
                name.setStyle("-fx-fill: white; -fx-font-size: 20");
                name.setText(item.getName());
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }
}
