package app.gui.administration;

import app.App;
import app.db.Project;
import app.db.User;
import app.exception.DatabaseException;
import app.gui.MyAlert;
import app.service.AdministrationService;
import app.transactions.UserTypeChangeTransaction;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class DialogConfirmController {

    /**
     * Getter a setter inštancie dialóg
     */
    private static DialogConfirmController instance;
    public static DialogConfirmController getInstance(){return instance;}

    /**
     * fullName - grafický komponent, ktorý zobrazuje celé meno užívateľa
     * userType - grafický komponent, ktorý zobrazuje rolu užívateľa
     * projectsListView - zoznam projektov, ktorých je užívateľ adminom
     */
    @FXML private Label fullName;
    @FXML private Label userType;
    @FXML private JFXListView<Pane> projectsListView;

    /**
     * user - užívateľ, ktorého rolu chceme zmeniť
     * projectsToSave - projekty, ktoré pridelíme užívateľovi
     * newType - nová rola, ktorú chceme užívateľovi nastaviť
     * stages - otvorené dialógové okná
     */
    private User user;
    private List<Project> projects;
    private List<String> projectsToAdd;
    private List<String> projectsToDelete;
    private User.USERTYPE newType;
    private List<Stage> stages;


    /**
     * Nastavenie dialógového okna - grafických komponentov
     * @param stages - nastavenie aktuálneho dialógového okna
     * @param user - používateľ, ktorého rolu chceme zmeniť
     * @param projects - všetky projekty užívateľa
     * @throws DatabaseException
     * @throws SQLException
     * @throws IOException
     */
    public void setDialog(List<Stage> stages, User user, List<Project> projects, List<String> projectsToAdd, List<String> projectsToDelete) throws IOException {
        instance = this;
        this.stages = stages;
        this.user = user;
        this.projects = projects;
        this.projectsToAdd = projectsToAdd;
        this.projectsToDelete = projectsToDelete;
        this.newType = User.USERTYPE.PROJECT_ADMIN;
        fullName.setText(user.getFullName());
        userType.setText("Projektový admin");

        for(Project project : projects) {
            projectsListView.getItems().add(setPane(project.getProjectNumber()));
        }
        projectsListView.setPrefWidth(App.getScene().getWidth());
        projectsListView.setPrefHeight(App.getScene().getHeight() - 80);
    }


    /**
     * Nastavenie položky zoznamu projektov, teda konkrétneho projektu
     * @param projectNumber - číslo projektu
     * @return
     * @throws IOException
     */
    private Pane setPane(String projectNumber) throws IOException {
        FXMLLoader loader = loadFXML("projectAdmin-show-projects-item");
        Pane pane = loader.load();
        loader.<DialogProjectsItemController>getController().setProjectNumber(projectNumber);
        pane.setId(projectNumber);
        return pane;
    }

    /**
     * Načítanie fxml súboru
     * @param fxml
     * @return
     * @throws IOException
     */
    private FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UsersAdministrationBoardController.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
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

    private void submitDialog(String text) throws DatabaseException, SQLException {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            if(!newType.equals(user.getUserType())) {
                UserTypeChangeTransaction.changeProjects(user, projectsToAdd, projectsToDelete);
            }
            UsersAdministrationItemController.getInstance().closeAllDialogs(stages);
        }
        else {
            alert.close();
        }

    }


}
