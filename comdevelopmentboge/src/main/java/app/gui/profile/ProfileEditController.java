package app.gui.profile;

import app.config.SignedUser;
import app.exception.ProfileChangeException;
import app.gui.MyAlert;
import app.gui.TabController;
import app.transactions.ProfileEditTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileEditController {

    /**
     * emailPrevious - email, ktorý bol nastavený pred jeho zmenou
     * passwordPrevious - heslo, ktorý bol nastavené pred jeho zmenou
     * passwordShown - boolovská hodnota zobrazeného hesla
     */
    private String emailPrevious;
    private String passwordPrevious;


    @FXML
    private TextField email;
    @FXML private TextField passwordVisible;
    @FXML private PasswordField passwordField;
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

        passwordField.setText("0000");
        passwordPrevious = SignedUser.getUser().getPassword();
    }



    @FXML
    private void emailEdit(MouseEvent event) {
        email_edit.setVisible(false);
        email_save.setVisible(true);
        email.setEditable(true);
    }

    @FXML
    private void emailSave(MouseEvent event) throws SQLException, ProfileChangeException {

        email.setEditable(false);
        email_edit.setVisible(true);
        email_save.setVisible(false);

        System.out.println("prev " + emailPrevious);
        System.out.println("now " + email.getText());
        if(!emailPrevious.equals(email.getText())) {
            ProfileEditTransaction.changeEmail(email.getText());
            email.setText(SignedUser.getUser().getEmail());
        }
    }

    @FXML
    private void passwordEdit(MouseEvent event) {
        passwordVisible.setEditable(true);
        passwordVisible.setVisible(true);
        password_edit.setVisible(false);
        passwordField.setVisible(false);
        password_save.setVisible(true);
        passwordVisible.requestFocus();
        passwordVisible.positionCaret(0);
    }

    @FXML
    private void passwordSave(MouseEvent event) throws SQLException, ProfileChangeException {
        passwordVisible.setEditable(false);
        passwordField.setVisible(true);
        passwordVisible.setVisible(false);
        password_edit.setVisible(true);
        password_save.setVisible(false);

        String password = org.apache.commons.codec.digest.DigestUtils.md5Hex(passwordVisible.getText());
        if(!passwordPrevious.equals(password)) {
            ProfileEditTransaction.changePassword(passwordVisible.getText());
            passwordField.setText("0000");
        }
        else {
            MyAlert.showWarning("Heslo je identické s aktuálnym heslom");
        }
    }

    @FXML
    private void close(MouseEvent event) throws IOException {
        TabController.getInstance().closeProfile();
    }

}
