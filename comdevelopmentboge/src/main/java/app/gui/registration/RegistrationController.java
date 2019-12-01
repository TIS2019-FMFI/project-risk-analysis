package app.gui.registration;

import app.App;
import app.exception.RegistrationException;
import app.transactions.Registration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;


public class RegistrationController {

    private boolean passwordShown = false;

    private Image eyeOn = new Image("app/images/eyeOn.png");
    private Image eyeOff = new Image("app/images/eyeOff.png");

    @FXML private TextField name;
    @FXML private TextField surname;
    @FXML private TextField email;
    @FXML private ImageView eye;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisible;


    private static RegistrationController registrationController = new RegistrationController();
    public static RegistrationController getRegistrationController() { return registrationController;}

    public void initialize() throws IOException {
        setScene();
        eye.setImage(eyeOff);
        passwordVisible.setVisible(false);
        passwordField.setVisible(true);
    }

    private static void setScene() throws IOException {
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
    private String getPasswordText() {
        return passwordShown ? passwordVisible.getText() : passwordField.getText();
    }

    @FXML
    private void confirmRegistration(MouseEvent event) {
        try {
            Registration.register(name.getText(),surname.getText(),email.getText(),getPasswordText());
            //TODO otvori sa stranka pre cakanie na potvrdenie
        } catch (RegistrationException e) {
            showAlert(e.getMessage());
        } catch (SQLException e) {
            showAlert("Nepodarilo sa spojenie s databázou. Vyskúšajte ešte raz");
        }
    }
    private void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }
    @FXML
    private void cancelRegistration(MouseEvent event) {
        //TODO zavolat login stranku
    }


}
