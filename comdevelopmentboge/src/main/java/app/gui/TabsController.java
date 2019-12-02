package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TabsController {


    @FXML private TabPane tabPane;
    private Tab main = new Tab();
    private Tab projects = new Tab();
    private Tab FEM = new Tab();

    @FXML AnchorPane profileChange = new AnchorPane(loadFXML("AProfileChange"));
    @FXML AnchorPane main_projects = new AnchorPane(loadFXML("AMainProjects"));
    @FXML AnchorPane main_FEM = new AnchorPane(loadFXML("AMainFEM"));

    public TabsController() throws IOException {
    }


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TabsController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void setTab(Tab tab, Node content, String name, boolean closable) {
        tab.setContent(content);
        tab.setText(name);
        tab.setClosable(closable);
    }


    public void initialize() throws IOException {
        setTab(main, profileChange, "Hlavná stránka", false);
        setTab(projects, main_projects, "Prehľad projektov", false);
        setTab(FEM, main_FEM, "FEM simulácie", false);
        tabPane.getTabs().addAll(main, projects, FEM);
    }
}
