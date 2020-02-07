package app.gui.administration;

import app.db.User;
import app.exception.DatabaseException;
import app.gui.MyAlert;
import app.transactions.UserTypeChangeTransaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class ChangeUserTypeController {

    /**
     * Getter a setter inštancie dialóg
     */
    private static ChangeUserTypeController instance;
    public static ChangeUserTypeController getInstance(){return instance;}

    /**
     * adminButton - radiobutton, ktorý označuje výber roly admina
     * femButton - radiobutton, ktorý označuje výber roly fem-kára
     * userButton - radiobutton, ktorý označuje výber roly bežného užívateľa
     */
    @FXML private RadioButton adminButton;
    @FXML private RadioButton femButton;
    @FXML private RadioButton userButton;

    /**
     * thisStage - aktuálne otvorené dialógové okno
     */
    Stage thisStage;
    /**
     * user - používateľ, ktorého rolu chceme zmeniť
     */
    private User user;

    /**
     * Funkcia nastaví radiobutton ako aktívny, podľa aktuálnej roly užívateľa
     * @param thisStage - nastavenie aktuálneho dialógového okna
     * @param user - nastavenie používateľa, ktorého rolu chceme zmeniť
     *
     */
    public void setSelected(Stage thisStage, User user) {
        instance = this;
        this.thisStage = thisStage;
        this.user = user;
        User.USERTYPE userType = user.getUserTypeU();

        if (userType.equals(User.USERTYPE.PROJECT_ADMIN) || userType.equals(User.USERTYPE.CENTRAL_ADMIN)) {
            adminButton.setSelected(true);
        }
        else if (userType.equals(User.USERTYPE.FEM)) {
            femButton.setSelected(true);
        }
        else if (userType.equals(User.USERTYPE.USER)) {
            userButton.setSelected(true);
        }
    }

    /**
     * Zatvorenie aktuálneho dialógového okna
     * @param event
     */
    @FXML
    private void close(MouseEvent event) {
        thisStage.close();
    }

    /**
     * Potvrdenie označenej roly užívateľa
     * @param event
     * @throws SQLException Pokiaľ sa nepodarí zmeniť rolu používateľa v databáze
     * @throws IOException
     * @throws DatabaseException
     */
    @FXML
    private void submit(MouseEvent event) throws SQLException, IOException {
        if(adminButton.isSelected()) {
            chooseAdminType();
        }
        else if (femButton.isSelected()) {
            changeUserType(User.USERTYPE.FEM);
            close(event);
        }
        else if (userButton.isSelected()) {
            changeUserType(User.USERTYPE.USER);
            close(event);
        }
        else {
            MyAlert.showWarning("Zvoľ typ užívateľa");
        }
    }

    /**
     * Spustí sa transakcia na zmenu roly používateľa - FEM-kár alebo bežný užívateľ
     * @param userType - rola, ktorú chceme nastaviť užívateľovi
     * @throws SQLException
     */
    private void changeUserType(User.USERTYPE userType) throws SQLException {
        if(!userType.equals(user.getUserType())) {
            UserTypeChangeTransaction.changeUserType(user, userType);
            user.setUserType(userType);
        }
    }

    /**
     * Zobrazenie dialógového okna na zmenu roly admin
     * @throws IOException
     */
    private void chooseAdminType() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("change-admin-type-dialog.fxml"));
        Parent parent = fxmlLoader.load();
        ChangeUserTypeAdminController dialogController = fxmlLoader.getController();

        Scene scene = new Scene(parent, 300, 400);
        Stage stage = new Stage();
        dialogController.setSelected(stage, user);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }


}
