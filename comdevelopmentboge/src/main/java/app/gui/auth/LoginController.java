package app.gui.auth;

import app.App;
import app.gui.home.HomeController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    double x, y;
    boolean fullSized;
    Image eyeOn = new Image ("file:eyeOn.png");
    Image eyeOff = new Image ("file:eyeOff.png");
    boolean passwordShown = false;
    @FXML private ImageView eye;

    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    private void min(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void max(MouseEvent event) {
        fullSized = !fullSized;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if(fullSized) {
            stage.setMaximized(true);
        }
        else {
            stage.setMaximized(false);
        }

    }

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    //zatial nefunguje
    @FXML
    private void showPassword(MouseEvent event) {
        passwordShown = !passwordShown;
        if(passwordShown) {
            eye.setImage(eyeOn);
        }
        else {
            eye.setImage(eyeOff);
        }

    }


    public void initialize() {

    }

    public void openMainPage(MouseEvent event) throws IOException {
        HomeController.getHomeController().initialize();
    }
}
