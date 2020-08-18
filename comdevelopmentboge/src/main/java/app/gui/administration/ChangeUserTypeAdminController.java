package app.gui.administration;

import app.db.User;
import app.exception.DatabaseException;
import app.gui.MyAlert;
import app.transactions.UserTypeChangeTransaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ChangeUserTypeAdminController {

    /**
     * Getter a setter instancie dialog
     */
    private static ChangeUserTypeAdminController instance;
    public static ChangeUserTypeAdminController getInstance(){return instance;}

    /**
     * projectAdminBtn - radiobutton, ktory oznacuje vyber roly projektoveho admina
     * centralAdminBtn - radiobutton, ktorý oznacuje vyber roly centralneho admina
     */
    @FXML private RadioButton projectAdminBtn;
    @FXML private RadioButton centralAdminBtn;


    /**
     * stages - otvorene dialogove okna
     */
     List<Stage> stages;

    /**
     * user - pouzivatel, ktoreho rolu chceme zmenit
     */
    private User user;

    /**
     * Funkcia nastaví radiobutton ako aktivny, podla aktualnej roly uzívatela
     * @param stages - nastavenie aktualneho dialogoveho okna
     * @param user - nastavenie pouzivatela, ktoreho rolu chceme zmenit
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
     * Zatvorenie aktualneho dialogoveho okna
     * @param event
     */
    @FXML
    private void close(MouseEvent event) {
        stages.get(stages.size()-1).close();
    }

    /**
     * Potvrdenie oznacenej roly uzivatela
     * @param event
     * @throws SQLException Pokial sa nepodari zmenit rolu pouzivatela v databaze
     * @throws IOException
     * @throws DatabaseException
     */
    @FXML
    private void submit(MouseEvent event) throws SQLException, IOException, DatabaseException, MessagingException {
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
     * @param userType - rola, ktoru chceme nastavit uzivatelovi
     * @throws SQLException
     */
    private void changeUserType(User.USERTYPE userType) throws SQLException, MessagingException, DatabaseException {
        if(!userType.equals(user.getUserTypeU())) {
            submitDialog("Chcete potvrdiť zmenu roly používateľa " + user.getFullName() + "?", userType);
        }
    }

    /**
     * Zobrazenie potvrdzovacieho dialogu pri zmene roly
     * @param text text v dialogu
     * @throws DatabaseException
     * @throws SQLException
     */
    private void submitDialog(String text, User.USERTYPE userType) throws DatabaseException, SQLException, MessagingException {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            UserTypeChangeTransaction.changeUserType(user, userType);
            user.setUserType(userType);
            UsersAdministrationItemController.getInstance().closeAllDialogs(stages);
        }
        else {
            alert.close();
        }

    }

    /**
     * Zobrazenie dialogoveho okna s projektami, ktorych adminom je uzivatel,
     * ktoreho rolu chceme zmenit
     * @throws IOException
     * @throws DatabaseException
     * @throws SQLException
     */
    private void showProjectAdminDialog() throws IOException, SQLException {
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
