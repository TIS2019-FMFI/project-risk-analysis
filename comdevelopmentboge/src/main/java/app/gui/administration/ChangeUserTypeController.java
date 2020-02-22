package app.gui.administration;

import app.db.User;
import app.exception.DatabaseException;
import app.gui.MyAlert;
import app.transactions.UserTypeChangeTransaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ChangeUserTypeController {

    /**
     * Getter a setter instancie dialog
     */
    private static ChangeUserTypeController instance;
    public static ChangeUserTypeController getInstance(){return instance;}

    /**
     * adminButton - radiobutton, ktory oznacuje vyber roly admina
     * femButton - radiobutton, ktory oznacuje vyber roly fem-kara
     * userButton - radiobutton, ktory oznacuje vyber roly bezneho uzivatela
     */
    @FXML private RadioButton adminButton;
    @FXML private RadioButton userButton;


    /**
     * stages - otvorene dialogove okna
     */
    List<Stage> stages;
    /**
     * user - pouzivatel, ktoreho rolu chceme zmenit
     */
    private User user;

    /**
     * Funkcia nastavi radiobutton ako aktivny, podla aktualnej roly uzivatela
     * @param stages - nastavenie aktualneho dialogoveho okna
     * @param user - nastavenie pouzivatela, ktoreho rolu chceme zmenit
     *
     */
    public void setSelected(List<Stage> stages, User user) {
        instance = this;
        this.stages = stages;
        this.user = user;
        User.USERTYPE userType = user.getUserTypeU();

        if (userType.equals(User.USERTYPE.PROJECT_ADMIN) || userType.equals(User.USERTYPE.CENTRAL_ADMIN)) {
            adminButton.setSelected(true);
        }
        else if (userType.equals(User.USERTYPE.USER)) {
            userButton.setSelected(true);
        }
    }

    /**
     * Zatvorenie aktualneho dialogoveho okna
     * @param event
     */
    @FXML
    void close(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    /**
     * Potvrdenie oznacenej roly uzivatela
     * @param event
     * @throws IOException
     * @throws DatabaseException
     */
    @FXML
    private void submit(MouseEvent event) throws IOException {
        if(adminButton.isSelected()) {
            chooseAdminType();
        }
        else if (userButton.isSelected()) {
            showConfirmDialog(User.USERTYPE.USER);
        }
        else {
            MyAlert.showWarning("Zvoľ typ užívateľa");
        }
    }

    /**
     * Zobrazenie potvrdzujuceho dialogu pre zmenu roly - bezny uzivatel
     * @param usertype typ uzivatela
     * @throws IOException
     */
    private void showConfirmDialog(User.USERTYPE usertype) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("administration-no-projects-confirm-dialog.fxml"));
        Parent parent = fxmlLoader.load();
        DialogNoProjectsConfirmDialogController dialogController = fxmlLoader.getController();

        Scene scene = new Scene(parent, 300, 200);
        Stage stage = new Stage();
        System.out.println("stage " + stage);
        stages.add(stage);
        UsersAdministrationItemController.getInstance().onCloseHandler(stages.get(stages.size()-1), this.stages);
        dialogController.setDialogNoProjects(stages, user, usertype);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Zobrazenie dialogoveho okna na zmenu roly admin
     * @throws IOException
     */
    private void chooseAdminType() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("change-admin-type-dialog.fxml"));
        Parent parent = fxmlLoader.load();
        ChangeUserTypeAdminController dialogController = fxmlLoader.getController();

        Scene scene = new Scene(parent, 300, 400);
        Stage stage = new Stage();
        stages.add(stage);
        UsersAdministrationItemController.getInstance().onCloseHandler(stages.get(stages.size()-1), this.stages);
        dialogController.setSelected(stages, user);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }


}
