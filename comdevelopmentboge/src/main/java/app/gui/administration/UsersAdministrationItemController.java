package app.gui.administration;


import app.config.SendMail;
import app.db.User;
import app.exception.DatabaseException;
import app.gui.MyAlert;
import app.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

public class UsersAdministrationItemController {

    /**
     * Getter a setter inštancie položky zoznamu užívateľov
     */
    private static UsersAdministrationItemController instance = new UsersAdministrationItemController();
    public static UsersAdministrationItemController getInstance(){return instance;}

    /**
     * userName - grafický komponent, ktorý zobrazuje meno užívateľa
     * email - grafický komponent, ktorý zobrazuje email užívateľa
     * userType - grafický komponent, ktorý zobrazuje rolu používateľa
     */
    @FXML private Label userName;
    @FXML private Label email;
    @FXML private Label userType;

    private User user;

    /**
     * Nastavenie konkrétneho používateľa
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
        userName.setText(user.getFullName());
    }

    /**
     * Nastavenie položky pre konkrétneho užívateľa
     * @param fullName - celé meno užívateľa
     */
    public void setInfo(String fullName) {
        try {
            this.user = UserService.getInstance().findUserByFullName(fullName);
            userName.setText(user.getFullName());
            email.setText(user.getEmail());
            userType.setText(userTypeToSlovak(user.getUserType()));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Preklad užívateľskej roly z angličtiny (uložené v databáze)
     * do slovenčiny (zobrazené v aplikácii)
     * @param userType - rola užívateľa
     * @return
     */
    public String userTypeToSlovak(String userType) {
        if(userType.equals("CENTRAL_ADMIN")) {
            return "Centrálny admin";
        }
        if(userType.equals("PROJECT_ADMIN")) {
            return "Projektový admin";
        }
        if(userType.equals("USER")) {
            return "Bežný užívateľ";
        }
        if(userType.equals("FEM")) {
            return "FEM-kár";
        }
        return "";
    }

    /**
     * Zmena roly užívateľa
     * @throws IOException
     */
    @FXML
    public void editUserType() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("change-user-type-dialog.fxml"));
        Parent parent = fxmlLoader.load();
        ChangeUserTypeController dialogController = fxmlLoader.<ChangeUserTypeController>getController();
        Scene scene = new Scene(parent, 300, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        dialogController.setSelected(stage, user);
        stage.showAndWait();
    }

    /**
     * Zobrazenie projektov, ktorých je adminom užívateľ
     * @throws IOException
     * @throws DatabaseException
     * @throws SQLException
     */
    @FXML
    public void showProjects() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("projectAdmin-show-projects.fxml"));
        Parent parent = fxmlLoader.load();
        DialogProjectsController dialogController = fxmlLoader.<DialogProjectsController>getController();
        dialogController.init(user);
        Scene scene = new Scene(parent, 300, 400);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Zobrazenie kontrolného dialógu pre generovanie nového hesla
     */
    @FXML
    public void generatePassword() {
        showQuestionDialog("Prajete si vygenerovať nové heslo?");
    }

    /**
     * Vygenerovanie nového hesla
     */
    public void generate() {
        String recepient = MyAlert.showInputDialog("Zadaj GMAIL-ovú adresu, na ktorú sa odošle vygenerované heslo");

        try {
            String generatedString = generateString();
            String md = org.apache.commons.codec.digest.DigestUtils.md5Hex(generatedString);
            SendMail.send(recepient, generatedString);
            user.setPassword(md);
            user.update();

            MyAlert.showSuccess("Heslo bolo úspešne vygenerované");
        } catch (MessagingException e) {
            MyAlert.showSuccess("Heslo sa nepodarilo vygenerovať");
        } catch (SQLException e) {
            MyAlert.showSuccess("Heslo sa nepodarilo vygenerovať");
        }
    }

    /**
     * Kontrolný dialóg pre vygenerovanie nového hesla
     * @param text
     */
    public void showQuestionDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            generate();
        }
        if (alert.getResult() == ButtonType.CLOSE) {
            alert.close();
        }
    }

    /**
     * Vygenerovanie náhodného hesla
     * @return
     */
    private String generateString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int numberDownLimit = 48; // number 0
        int numberUpperLimit = 57; // number 9

        int letterStringLength = 6;
        int numberStringLength = 2;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(letterStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        generatedString += random.ints(numberDownLimit, numberUpperLimit + 1)
                .limit(numberStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
       return generatedString;
    }
}
