package app.gui;

import app.App;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class TabController {

    private static TabController instance = new TabController();
    public static TabController getInstance(){return instance;}

    private VBox mainBox;
    private TabPane mainTabPane;


    public void init() throws IOException {
        mainBox = new VBox();
        mainBox.getChildren().add(loadFXML("bar/main-page-menu-bar"));
        mainTabPane = (TabPane) loadFXML("tab-pane");
        mainBox.getChildren().add(mainTabPane);

        App.setRoot(mainBox);

        setOnSelectedListener();
    }


    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TabController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void setOnSelectedListener() throws IOException {
        ObservableList<Tab> tabs = mainTabPane.getTabs();
        for (Tab tab : tabs) {
            if (tab.getId().equals("projectTab")) {
                tab.setOnSelectionChanged(event -> {
                    if (tab.isSelected()) {
                        try {
                            selectProjectTab();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if(tab.getId().equals("mainPageTab")){
                tab.setOnSelectionChanged(event -> {
                    if (tab.isSelected()) {
                        try {
                            selectMainPageTab();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public void selectProjectTab() throws IOException {
        setMenuBar("bar/project-list-menu-bar.fxml");

        ObservableList<Tab> tabs = mainTabPane.getTabs();
        for (Tab tab : tabs) {
            if (tab.getId().equals("projectTab")) {
                try {
                    tab.setContent(loadFXML("project/project-list-board"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void selectMainPageTab() throws IOException {
        setMenuBar("bar/main-page-menu-bar.fxml");
    }

    public void setMenuBar(String fxml) throws IOException {
        mainBox.getChildren().remove(0);
        mainBox.getChildren().add(0, FXMLLoader.load(TabController.class.getResource(fxml)));

    }


}
