package app.gui.auth;

import app.App;
import app.config.SignedUser;
import app.gui.MyAlert;
import app.gui.TabController;
import app.gui.home.HomeController;
import app.gui.registration.RegistrationController;
import app.transactions.LoginTransaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    /**
     * passwordShown - boolovská hodnota zobrazenia hesla
     */
    private boolean passwordShown;

    /**
     * eyeOn - nepreškrtnutý znak oka, heslo je viditeľné
     * eyeOff - preškrtnutý znak oka, heslo nie je viditeľné
     */
    private Image eyeOn = new Image("app/images/eyeOn.png");
    private Image eyeOff = new Image("app/images/eyeOff.png");

    /**
     * email - grafický komponent, ktorý zobrazuje email užívateľa
     * eye - grafický komponent, ktorý zobrazuje obrázok oka, teda preškrtnuté alebo nepreškrtnuté oko
     * passwordField - grafický komponent, ktorý zobrazuje heslo vo forme bodiek
     * passwordVisible - grafický komponent, ktorý zobrazuje viditeľné heslo
     */
    @FXML private TextField email;
    @FXML private ImageView eye;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisible;

    /**
     * Zobrazenie alebo skrytie hesla
     * @param event
     */
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

    /**
     * Nastavenie grafických komponentov pri úvodnej obrazovke
     */
    public void initialize() {
        passwordShown = false;
        eye.setImage(eyeOff);
        passwordVisible.setVisible(false);
        passwordField.setVisible(true);
    }

    /**
     * Otvorenie registrácie
     * @param event
     * @throws IOException
     */
    public void openRegistration(MouseEvent event) throws  IOException {
        App.setRoot("gui/registration/registration");
    }


    /**
     * Získanie hesla z grafického komponentu
     * @return
     */
    private String getPasswordText() {
        return passwordShown ? passwordVisible.getText() : passwordField.getText();
    }


    /**
     * Prihlásenie užívateľa a zobrazenie stránky podľa prihláseného užívateľa
     * @param event
     */
    @FXML
    private void login(MouseEvent event) {
        try {
            LoginTransaction.login(email.getText(),getPasswordText());
            //udaje su spravne ale nie je este schvaleny
            if (!SignedUser.getUser().getApproved()) {
                openWaitingPage();
            } else {
                FXMLLoader.load(TabController.class.getResource("main-box.fxml"));
            }

        } catch (LoginException e) {
            MyAlert.showError(e.getMessage());
        } catch (SQLException e) {
            MyAlert.showError("Nepodarilo sa spojenie s databázou. Vyskúšajte ešte raz");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Otvorenie stránky, pokiaľ užívateľ po registrácii ešte nie je schválený
     * @throws IOException
     */
    private void openWaitingPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationController.class.getResource("registration-waiting.fxml"));
        App.setRoot((Parent) fxmlLoader.load());
    }


}
