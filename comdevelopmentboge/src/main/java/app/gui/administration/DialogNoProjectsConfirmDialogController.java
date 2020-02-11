package app.gui.administration;

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
     * Getter a setter instancie dialog
     */
    private static DialogNoProjectsConfirmDialogController instance;
    public static DialogNoProjectsConfirmDialogController getInstance(){return instance;}

    /**
     * fullName - graficky komponent, ktory zobrazuje cele meno uzivatela
     * userType - graficky komponent, ktory zobrazuje rolu uzivatela
     * projectsListView - zoznam projektov, ktorych je uzivatel adminom
     */
    @FXML private Label fullName;
    @FXML private Label userType;

    /**
     * user - uzivatel, ktoreho rolu chceme zmenit
     * projectsToSave - projekty, ktore pridelime uzivatelovi
     * newType - nova rola, ktoru chceme uzivatelovi nastavit
     * stages - otvorene dialogove okna
     */
    private User user;
    private User.USERTYPE newType;
    private List<Stage> stages;


    /**
     *
     * @param stages aktualne otvorene dialogove okna
     * @param user pouzivatel, ktoremu menime rolu
     * @param newType typ roly, ktoru chceme uzivatelovi pridelit
     * @throws IOException chyba v grafickom komponente
     */
    public void setDialogNoProjects(List<Stage> stages, User user, User.USERTYPE newType) throws IOException {
        instance = this;
        this.stages = stages;
        this.user = user;
        this.newType = newType;
        fullName.setText(user.getFullName());
        userType.setText(UsersAdministrationItemController.getInstance().userTypeToSlovak(newType.toString()));
    }

    /**
     * Zatvorenie aktualneho dialogoveho okna
     * @param event
     */
    @FXML
    void close(MouseEvent event) {
        stages.get(stages.size()-1).close();
    }

    /**
     * Spustenie transakcie na zmenu roly a projektov uzivatela
     * @param event
     * @throws SQLException
     * @throws DatabaseException
     */
    @FXML
    private void submit(MouseEvent event) throws SQLException {
        submitDialog("Chcete potvrdiť zmeny používateľa " + user.getFullName() + "?");
    }

    /**
     * Zobrazenie potvrdzovacieho dialogu
     * @param text text v dialogu
     * @throws SQLException chyba pri ziskavani dat z databazy
     */
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
