package app.gui.auth;

import app.App;
import app.gui.TabController;
import app.gui.home.HomeController;
import app.transactions.LoginTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    boolean passwordShown;

    Image eyeOn = new Image("app/images/eyeOn.png");
    Image eyeOff = new Image("app/images/eyeOff.png");

    @FXML private TextField email;
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

    public void openRegistration(MouseEvent event) throws  IOException {
        App.setRoot("gui/registration/registration");
    }


    private String getPasswordText() {
        return passwordShown ? passwordVisible.getText() : passwordField.getText();
    }

    @FXML
    private void login(MouseEvent event) {
        try {
            LoginTransaction.login(email.getText(),getPasswordText());
            TabController.getInstance().init();
        } catch (LoginException e) {
            showAlert(e.getMessage());
        } catch (SQLException e) {
            showAlert("Nepodarilo sa spojenie s databázou. Vyskúšajte ešte raz");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

}
