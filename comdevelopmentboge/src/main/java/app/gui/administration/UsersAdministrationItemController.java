package app.gui.administration;


import app.config.SendMail;
import app.config.SignedUser;
import app.db.User;
import app.gui.MyAlert;
import app.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UsersAdministrationItemController {

    /**
     * Getter a setter instancie polozky zoznamu uzivatelov
     */
    private static UsersAdministrationItemController instance = new UsersAdministrationItemController();
    public static UsersAdministrationItemController getInstance(){return instance;}

    /**
     * userName - graficky komponent, ktory zobrazuje meno uzivatela
     * email - graficky komponent, ktory zobrazuje email uzivatela
     * userType - graficky komponent, ktory zobrazuje rolu pouzivatela
     */
    @FXML private Label userName;
    @FXML private Label email;
    @FXML private Label userType;

    private User user;

    List<Stage> stages = new ArrayList<>();

    /**
     * Nastavenie konkretneho pouzivatela
     * @param user pouzivatel, ktoremu menime rolu
     */
    public void setUser(User user) {
        this.user = user;
        userName.setText(user.getFullName());
    }

    /**
     * Nastavenie polozky pre konkretneho uzivatela
     * @param emailTxt - email užívateľa
     */
    public void setInfo(String emailTxt) {
        try {
            this.user = UserService.getInstance().findUserByEmail(emailTxt);
            userName.setText(user.getFullName());
            email.setText(user.getEmail());
            userType.setText(userTypeToSlovak(user.getUserType()));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Preklad uzivatelskej roly z anglictiny (uložene v databaze)
     * do slovenciny (zobrazene v aplikacii)
     * @param userType - rola uzivatela
     * @return slovensky preklad uzivatelskej roly
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
     * Zmena roly uzivatela
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    public void editUserType() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("change-user-type-dialog.fxml"));
        Parent parent = fxmlLoader.load();
        ChangeUserTypeController dialogController = fxmlLoader.<ChangeUserTypeController>getController();
        Scene scene = new Scene(parent, 300, 400);
        Stage stage = new Stage();
        stages.add(stage);
        stage.setScene(scene);
        dialogController.setSelected(stages, user);
        stage.showAndWait();
    }

    /**
     * Zobrazenie projektov, ktorych je adminom uzivatel
     * @throws IOException chyba v grafickom komponente
     * @throws SQLException chyba pri ziskavani dat z databazy
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
     * Zobrazenie kontrolneho dialogu pre generovanie noveho hesla
     */
    @FXML
    public void generatePassword() {
        if(MyAlert.showConfirmationDialog("Prajete si vygenerovať nové heslo?")) {
            generate();
        }
    }

    /**
     * Vygenerovanie noveho hesla
     */
    public void generate() {

        try {
            String generatedString = generateString();
            String md = org.apache.commons.codec.digest.DigestUtils.md5Hex(generatedString);
            SendMail.sendNewPassword(user.getEmail(), generatedString);
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
     * Vygenerovanie nahodneho hesla
     * @return nahodne vygenerovany text
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

    /**
     * Zatvorenie dialogov
     */
    void closeAllDialogs(List<Stage> stages) {
        for(Stage stage : stages) {
            System.out.println("closing " + stage);
            stage.close();
        }
    }

    /**
     * Nastavenie zatvorenia dialogov
     * @param stage - nastavime, čo sa stane po zatvoreni (kliknuti na X) tohto okna
     * @param stages - vsetky otvorene dialogove okna
     */
    void onCloseHandler(Stage stage, List<Stage> stages) {
        stage.setOnCloseRequest(E -> {
            closeAllDialogs(stages);
        });
    }
}
