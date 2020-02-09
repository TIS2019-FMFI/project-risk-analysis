package app.gui.profile;

import app.config.SignedUser;
import app.exception.ProfileChangeException;
import app.gui.MyAlert;
import app.gui.TabController;
import app.transactions.ProfileEditTransaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileEditController {
    private String emailPrevious;
    private String passwordPrevious;
    private boolean passwordShown = false;

    private Image eyeOn = new Image("app/images/eyeOn.png");
    private Image eyeOff = new Image("app/images/eyeOff.png");
    private Image edit = new Image("app/images/edit.png");
    private Image confirm = new Image("app/images/confirm.png");

    @FXML
    private TextField email;
    @FXML private TextField passwordVisible;
    @FXML private PasswordField passwordField;
    @FXML private ImageView eye;
    @FXML private ImageView email_edit;
    @FXML private ImageView password_edit;
    @FXML private ImageView email_save;
    @FXML private ImageView password_save;
    @FXML private Label name;


    private static ProfileEditController instance = new ProfileEditController();
    public static ProfileEditController getInstance(){return instance;}

    @FXML
    private void initialize() {
        setFields();
    }

    private void setFields() {
        name.setText(SignedUser.getUser().getFullName());

        email.setText(SignedUser.getUser().getEmail());
        email.setEditable(false);
        passwordVisible.setEditable(false);
        passwordField.setEditable(false);


        emailPrevious = SignedUser.getUser().getEmail();

        //TODO prekonvertovat z MD5 na heslo. da sa to vobec ? - NEDA SA
        passwordField.setText("0000");
        passwordPrevious = SignedUser.getUser().getPassword();
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
        email_edit.setVisible(false);
        email_save.setVisible(true);
        email.setEditable(true);
    }

    @FXML
    private void emailSave(MouseEvent event) {

        email.setEditable(false);
        email_edit.setVisible(true);
        email_save.setVisible(false);

        if(!emailPrevious.equals(email)) {
            try {
                ProfileEditTransaction.changeEmail(email.getText());
                MyAlert.showSuccess("Email bol úspešne zmenený");
            } catch (ProfileChangeException e) {
                MyAlert.showError(e.getMessage());
            } catch (SQLException e) {
                MyAlert.showError("Nepodarilo sa spojenie s databázou. Vyskúšajte ešte raz");
            }
            email.setText(SignedUser.getUser().getEmail());
        }
    }

    @FXML
    private void passwordEdit(MouseEvent event) {
        passwordVisible.setEditable(true);
        passwordVisible.setVisible(true);
        password_edit.setVisible(false);
        passwordField.setVisible(false);
        //eye.setVisible(true);
        password_save.setVisible(true);
    }

    @FXML
    private void passwordSave(MouseEvent event) {
        passwordVisible.setEditable(false);
        passwordField.setVisible(true);
        passwordVisible.setVisible(false);
        password_edit.setVisible(true);
        password_save.setVisible(false);

        if(!passwordPrevious.equals(passwordVisible)) {
            try {
                ProfileEditTransaction.changePassword(passwordVisible.getText());
                MyAlert.showSuccess("Heslo bolo úspešne zmenené");
            } catch (ProfileChangeException e) {
                MyAlert.showError(e.getMessage());
            } catch (SQLException e) {
                MyAlert.showError("Nepodarilo sa spojenie s databázou. Vyskúšajte ešte raz");
            }
            passwordField.setText("0000");
        }
    }



    @FXML
    private void close(MouseEvent event) {
        TabController.getInstance().closeProfile();
    }

}
