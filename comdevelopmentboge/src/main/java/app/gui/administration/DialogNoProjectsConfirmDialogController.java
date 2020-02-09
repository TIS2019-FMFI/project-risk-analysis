package app.gui.administration;

import app.db.Project;
import app.db.User;
import app.exception.DatabaseException;
import app.transactions.UserTypeChangeTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DialogNoProjectsConfirmDialogController {

    /**
     * Getter a setter inštancie dialóg
     */
    private static DialogNoProjectsConfirmDialogController instance;
    public static DialogNoProjectsConfirmDialogController getInstance(){return instance;}

    /**
     * fullName - grafický komponent, ktorý zobrazuje celé meno užívateľa
     * userType - grafický komponent, ktorý zobrazuje rolu užívateľa
     * projectsListView - zoznam projektov, ktorých je užívateľ adminom
     */
    @FXML private Label fullName;
    @FXML private Label userType;

    /**
     * user - užívateľ, ktorého rolu chceme zmeniť
     * projectsToSave - projekty, ktoré pridelíme užívateľovi
     * newType - nová rola, ktorú chceme užívateľovi nastaviť
     * stages - otvorené dialógové okná
     */
    private User user;
    private User.USERTYPE newType;
    private List<Stage> stages;


    public void setDialogNoProjects(List<Stage> stages, User user, User.USERTYPE newType) throws IOException {
        instance = this;
        this.stages = stages;
        this.user = user;
        this.newType = newType;
        fullName.setText(user.getFullName());
        userType.setText(UsersAdministrationItemController.getInstance().userTypeToSlovak(newType.toString()));
    }

    /**
     * Zatvorenie aktuálneho dialógového okna
     * @param event
     */
    @FXML
    private void close(MouseEvent event) {
        stages.get(stages.size()-1).close();
    }

    /**
     * Spustenie transakcie na zmenu roly a projektov užívateľa
     * @param event
     * @throws SQLException
     * @throws DatabaseException
     */
    @FXML
    private void submit(MouseEvent event) throws SQLException, DatabaseException {
        submitDialog("Chcete potvrdiť zmeny používateľa " + user.getFullName() + "?");
    }

    private void submitDialog(String text) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            if(!newType.equals(user.getUserType())) {
                UserTypeChangeTransaction.changeUserType(user, newType);
            }
            UsersAdministrationItemController.getInstance().closeAllDialogs(stages);
        }
        else {
            alert.close();
        }

    }

}
