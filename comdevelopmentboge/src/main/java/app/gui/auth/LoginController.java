package app.gui.auth;

import app.gui.home.HomeController;
import app.gui.registration.RegistrationController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginController {

    boolean passwordShown;

    Image eyeOn = new Image("app/images/eyeOn.png");
    Image eyeOff = new Image("app/images/eyeOff.png");

    @FXML private ImageView eye;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisible;

    @FXML
    private void showPassword(MouseEvent event) {
        passwordShown = !passwordShown;
        if(passwordShown) {
            eye.setImage(eyeOn);
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(true);
            passwordField.setVisible(false);
        }
        else {
            eye.setImage(eyeOff);
            passwordField.setText(passwordVisible.getText());
            passwordField.setVisible(true);
            passwordVisible.setVisible(false);
        }

    }


    public void initialize() {
        passwordShown = false;
        eye.setImage(eyeOff);
        passwordVisible.setVisible(false);
        passwordField.setVisible(true);
    }

    //TODO - nacitanie registracie
    public void openRegistration(MouseEvent event) throws  IOException {
        RegistrationController.getRegistrationController().initialize();
    }

    public void openMainPage(MouseEvent event) throws IOException {
        HomeController.getHomeController().initialize();
    }


    public void login(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Nesprávny tvar emailu", ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
           alert.close();
        }
    }

}
