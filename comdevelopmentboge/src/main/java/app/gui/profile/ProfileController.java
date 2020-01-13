package app.gui.profile;

import app.App;
import app.config.SignedUser;
import app.gui.registration.RegistrationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.IOException;

public class ProfileController {

    private String emailPrevious;
    private String passwordPrevious;
    private boolean passwordShown = false;

    private Image eyeOn = new Image("app/images/eyeOn.png");
    private Image eyeOff = new Image("app/images/eyeOff.png");
    private Image edit = new Image("app/images/edit.png");
    private Image confirm = new Image("app/images/confirm.png");

    @FXML private TextField email;
    @FXML private TextField passwordVisible;
    @FXML private PasswordField passwordField;
    @FXML private ImageView eye;
    @FXML private ImageView email_edit;
    @FXML private ImageView password_edit;
    @FXML private ImageView email_save;
    @FXML private ImageView password_save;
    @FXML private ImageView close;
    @FXML private Label name;


    private static ProfileController instance = new ProfileController();
    public static ProfileController getInstance(){return instance;}


    public void init() throws IOException {
        setScene();

    }


    private void setFields() {
        name.setText(SignedUser.getUser().getFullName());

        email.setText(SignedUser.getUser().getEmail());
        emailPrevious = SignedUser.getUser().getEmail();

        //TODO prekonvertovat z MD5 na heslo. da sa to vobec ?
        passwordField.setText(SignedUser.getUser().getPassword());
        passwordPrevious = SignedUser.getUser().getPassword();
    }

    private void setScene() throws IOException {
        Parent parent = loadFXML("profile");
        App.setRoot(parent);
        setFields();
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ProfileController.class.getResource(fxml + ".fxml"));
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
    private void emailEdit(MouseEvent event) {
        emailPrevious = email.getText();

        email.setEditable(true);
        email_edit.setVisible(false);
        email_save.setVisible(true);
    }

    @FXML
    private void cancelEdit(MouseEvent event) {
        email.setText(emailPrevious);
        passwordField.setText(passwordPrevious);

        email.setEditable(false);
        passwordField.setEditable(false);
        passwordVisible.setEditable(false);
    }


}
