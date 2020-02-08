package app.gui;

import app.App;
import app.config.SignedUser;
import app.db.User;
import app.gui.project.ProjectController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.ParseException;


public class TabController {

    private static TabController instance;
    public static TabController getInstance(){return instance;}

    @FXML
    private VBox mainBox;
    private TabPane mainTabPane;


    public void initialize() throws IOException {

        instance = this;

        if(SignedUser.getUser().getUserType().equals("CENTRAL_ADMIN")){
            mainBox.getChildren().add(loadFXML("bar/admin-main-page-menu-bar"));
        } else{
            mainBox.getChildren().add(loadFXML("bar/main-page-menu-bar"));
        }
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
                    tab.setContent(loadFXML("project/project-board"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void selectUsersAdministration() throws IOException {
        ObservableList<Tab> tabs = mainTabPane.getTabs();
        for (Tab tab : tabs) {
            if (tab.isSelected()) {
                try {
                    tab.setContent(loadFXML("administration/users-administration-board"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeUsersAdministration() {
        ObservableList<Tab> tabs = mainTabPane.getTabs();
        for (Tab tab : tabs) {
            if (tab.isSelected()) {
                if (tab.getId().equals("projectTab")) {
                    try {
                        selectProjectTab();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        selectMainPageTab();
                        tab.setContent(loadFXML("home/admin-main"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void selectProjectDetailsTab(String projectDef) throws IOException{
        setMenuBar("bar/project-details-menu-bar.fxml");

        ObservableList<Tab> tabs = mainTabPane.getTabs();
        for (Tab tab : tabs) {
            if (tab.getId().equals("projectTab")) {
                try {
                    tab.setContent(loadFXML("project/project-details-board"));
                    ProjectController.getProjectController().displayProjectData(projectDef);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void selectMainPageTab() throws IOException {

        if(SignedUser.getUser().getUserType().equals("CENTRAL_ADMIN")){
            setMenuBar("bar/admin-main-page-menu-bar.fxml");
        } else{
            setMenuBar("bar/main-page-menu-bar.fxml");
        }
    }

    public void setMenuBar(String fxml) throws IOException {
        mainBox.getChildren().remove(0);
        mainBox.getChildren().add(0, FXMLLoader.load(TabController.class.getResource(fxml)));

    }
}
