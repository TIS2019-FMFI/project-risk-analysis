package app.gui.registration;

import app.App;
import app.config.SignedUser;
import app.exception.DatabaseException;
import app.exception.MyException;
import app.exception.RegistrationException;
import app.gui.MyAlert;
import app.gui.TabController;
import app.gui.auth.LoginController;
import app.service.LogService;
import app.transactions.Registration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
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



    private static RegistrationController instance = new RegistrationController();
    public static RegistrationController getInstance(){return instance;}

    public void init() throws IOException {
        setScene();

    }

    private void setScene() throws IOException {
        Parent parent = loadFXML("registration");
        App.setRoot(parent);
    }

    private Parent loadFXML(String fxml) throws IOException {
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
    private void confirmRegistration(MouseEvent event) throws IOException {
        try {
            Registration.register(name.getText(),surname.getText(),email.getText(),getPasswordText());
            openWaitingPage();
        } catch (MyException e) {
            MyAlert.showError(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            MyAlert.showError(DatabaseException.ERROR);
            e.printStackTrace();
        }
    }
    private void openWaitingPage() throws IOException {
        App.setRoot(loadFXML("registration-waiting"));
    }

    @FXML
    private void cancelRegistration(MouseEvent event) throws IOException {
        App.setRoot("gui/auth/login");

    }
    @FXML
    private void refreshData(MouseEvent event) throws IOException {
        //tato metoda sa da zavolat len ak sa uspesne zaregistroval, preto bude nastaveny SignedUser
        try {
            if (Registration.isRegistrationApproved(SignedUser.getUser())) {
                SignedUser.setUser(null);
                App.setRoot("gui/auth/login");
                //FXMLLoader.load(TabController.class.getResource("main-box.fxml"));

            }
        } catch (SQLException e) {
            MyAlert.showError(DatabaseException.ERROR);
            e.printStackTrace();
        }
    }


}
