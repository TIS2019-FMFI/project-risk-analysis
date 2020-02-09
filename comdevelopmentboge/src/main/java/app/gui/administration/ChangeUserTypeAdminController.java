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
import java.util.List;


public class ChangeUserTypeAdminController {

    /**
     * Getter a setter inštancie dialóg
     */
    private static ChangeUserTypeAdminController instance;
    public static ChangeUserTypeAdminController getInstance(){return instance;}

    /**
     * projectAdminBtn - radiobutton, ktorý označuje výber roly projektového admina
     * centralAdminBtn - radiobutton, ktorý označuje výber roly centrálneho admina
     */
    @FXML private RadioButton projectAdminBtn;
    @FXML private RadioButton centralAdminBtn;


    /**
     * thisStage - aktuálne otvorené dialógové okno
     */
     List<Stage> stages;

    /**
     * user - používateľ, ktorého rolu chceme zmeniť
     */
    private User user;

    /**
     * Funkcia nastaví radiobutton ako aktívny, podľa aktuálnej roly užívateľa
     * @param stages - nastavenie aktuálneho dialógového okna
     * @param user - nastavenie používateľa, ktorého rolu chceme zmeniť
     *
     */
    public void setSelected(List<Stage> stages, User user) {
        instance = this;
        this.user = user;
        this.stages = stages;

        User.USERTYPE userType = user.getUserTypeU();
        if (userType.equals(User.USERTYPE.PROJECT_ADMIN)) {
            projectAdminBtn.setSelected(true);
        }
        else if (userType.equals(User.USERTYPE.CENTRAL_ADMIN)) {
            centralAdminBtn.setSelected(true);
        }
    }


    /**
     * Zatvorenie aktuálneho dialógového okna
     * @param event
     */
    @FXML
    private void close(MouseEvent event) {
        stages.get(0).close();
    }

    /**
     * Potvrdenie označenej roly užívateľa
     * @param event
     * @throws SQLException Pokiaľ sa nepodarí zmeniť rolu používateľa v databáze
     * @throws IOException
     * @throws DatabaseException
     */
    @FXML
    private void submit(MouseEvent event) throws SQLException, IOException, DatabaseException {
        if(projectAdminBtn.isSelected()) {
            showProjectAdminDialog();
        }
        else if (centralAdminBtn.isSelected()) {
            changeUserType(User.USERTYPE.CENTRAL_ADMIN);
            close(event);
        }
        else {
            MyAlert.showWarning("Zvoľ typ admina");
        }

    }


    /**
     * Spustí sa transakcia na zmenu roly admina
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
     * Zobrazenie dialógového okna s projektami, ktorých adminom je užívateľ,
     * ktorého rolu chceme zmeniť
     * @throws IOException
     * @throws DatabaseException
     * @throws SQLException
     */
    private void showProjectAdminDialog() throws IOException, DatabaseException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("projects-administration-dialog.fxml"));
        Parent parent = fxmlLoader.load();
        DialogProjectsController dialogController = fxmlLoader.getController();
        Scene scene = new Scene(parent, 300, 400);
        Stage stage = new Stage();
        stages.add(stage);
        UsersAdministrationItemController.getInstance().onCloseHandler(stages.get(stages.size()-1), this.stages);
        dialogController.setProjectAdminDialog(stages, user);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }


}
