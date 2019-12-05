package app.gui;

import app.App;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class TabController {

    private static TabController instance = new TabController();
    public static TabController getInstance(){return instance;}

    @FXML
    private VBox mainBox;

    @FXML
    private TabPane mainTabPane;


    public void init() throws IOException {
        setMainScene();
    }

    public void setMainScene() throws IOException {
        Parent parent = loadFXML("main");
        App.setRoot(parent);
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TabController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    private boolean isProjectTabSelected(){
        ObservableList<Tab> tabs =  mainTabPane.getTabs();
        for(Tab tab : tabs){
            if(tab.getId().equals("projectTab")){
                if(tab.isSelected()) return true;
            }
        }
        return false;
    }

    public void selectProjectTab() throws IOException {
        if(isProjectTabSelected()) {
            setMenuBar("bar/project-list-menu-bar.fxml");
        }
    }



    private boolean isMainPageTabSelected(){
        ObservableList<Tab> tabs =  mainTabPane.getTabs();
        for(Tab tab : tabs){
            if(tab.getId().equals("mainPageTab")){
                if(tab.isSelected()) return true;
            }
        }
        return false;
    }

    public void selectMainPageTab() throws IOException {
        if(isMainPageTabSelected()) {
            setMenuBar("bar/main-page-menu-bar.fxml");
        }
    }

    public void setMenuBar(String fxml) throws IOException {
        mainBox.getChildren().remove(0);
        mainBox.getChildren().add(0, FXMLLoader.load(TabController.class.getResource(fxml)));
    }


}
