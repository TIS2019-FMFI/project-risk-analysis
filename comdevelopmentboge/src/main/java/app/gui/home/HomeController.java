package app.gui.home;

import app.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Flow;

public class HomeController {

    private static HomeController homeController = new HomeController();
    public static HomeController getHomeController() { return homeController;}

    public void initialize() throws IOException {
        setScene();
        setNotificationScene(null);
    }

    public static void setScene() throws IOException {
        Parent parent = loadFXML("admin-main");
        App.setRoot(parent);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setNotificationScene(List<Scene> notifications) throws IOException {

       SubScene subScene0 = (SubScene) App.getScene().lookup("#notification0");
       Parent parent = loadFXML("notification-project");
       subScene0.setRoot(parent);

    }

}
