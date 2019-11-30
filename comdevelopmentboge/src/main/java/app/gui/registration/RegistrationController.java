package app.gui.registration;

import app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;


public class RegistrationController {

    boolean passwordShown;

    Image eyeOn = new Image("app/images/eyeOn.png");
    Image eyeOff = new Image("app/images/eyeOff.png");

    @FXML private ImageView eye;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisible;

    private static RegistrationController registrationController = new RegistrationController();
    public static RegistrationController getRegistrationController() { return registrationController;}

    public void initialize() throws IOException {
        setScene();
        passwordShown = false;
        eye.setImage(eyeOff);
        passwordVisible.setVisible(false);
        passwordField.setVisible(true);
    }

    public static void setScene() throws IOException {
        Parent parent = loadFXML("registration");
        App.setRoot(parent);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


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


}
