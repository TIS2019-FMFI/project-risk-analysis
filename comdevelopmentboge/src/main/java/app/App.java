package app;

import app.config.Configuration;
import app.config.PropertiesManager;
import app.importer.Generate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    public static Scene getScene(){return scene;}
    private static PropertiesManager propertiesManager;


    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("gui/auth/login"));
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();

        configure();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void setRoot(Parent parent) {
        scene.setRoot(parent);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void configure() throws IOException {

        propertiesManager = new PropertiesManager();
        Configuration.connect();
        // Generate.createAndGenerate();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }

    public static PropertiesManager getPropertiesManager() {
        return propertiesManager;
    }
}