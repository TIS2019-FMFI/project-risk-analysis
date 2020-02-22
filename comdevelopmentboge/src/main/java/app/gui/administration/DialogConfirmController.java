package app.gui.administration;

import app.App;
import app.db.Project;
import app.db.User;
import app.exception.DatabaseException;
import app.gui.MyAlert;
import app.service.AdministrationService;
import app.service.ProjectService;
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
import java.util.stream.Collectors;


public class DialogConfirmController {

    /**
     * Getter a setter instancie dialog
     */
    private static DialogConfirmController instance;
    public static DialogConfirmController getInstance(){return instance;}

    /**
     * fullName - graficky komponent, ktory zobrazuje cele meno uzivatela
     * userType - graficky komponent, ktory zobrazuje rolu uzivatela
     * projectsListView - zoznam projektov, ktorych je uzivatel adminom
     */
    @FXML private Label fullName;
    @FXML private Label userType;
    @FXML private JFXListView<Pane> projectsListView;

    /**
     * user - uzivatel, ktoreho rolu chceme zmenit
     * projectsToSave - projekty, ktore pridelime uzivatelovi
     * newType - nova rola, ktoru chceme uzivatelovi nastavit
     * stages - otvorene dialogove okna
     */
    private User user;
    private List<Project> projects;
    private List<String> projectsToAdd;
    private List<String> projectsToDelete;
    private User.USERTYPE newType;
    private List<Stage> stages;


    /**
     * Nastavenie dialogoveho okna - grafickych komponentov
     * @param stages - nastavenie aktualneho dialogoveho okna
     * @param user - pouzivatel, ktoreho rolu chceme zmenit
     * @param projects - vsetky projekty uzivatela
     * @throws IOException chyba s grafickym komponentom
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
     * Nastavenie polozky zoznamu projektov, teda konkretneho projektu
     * @param projectNumber - cislo projektu
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
     * Načítanie fxml suboru
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
    void close(MouseEvent event) {
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
        if((projectsToAdd.size() == 0 || sameToAdd()) && projectsToDelete.size() == 0) {
            if(newType.equals(user.getUserTypeU())) {
                submitDialogNoChanges("Neboli zistené žiadne zmeny");
            }
            else {
                submitDialog("Chcete potvrdiť zmeny používateľa " + user.getFullName() + "?");
            }
        }
        else {
            submitDialog("Chcete potvrdiť zmeny používateľa " + user.getFullName() + "?");
        }
    }

    /**
     * Zobrazenie potvrdzovacieho dialogu pri role admin
     * @param text text v dialogu
     * @throws DatabaseException
     * @throws SQLException
     */
    private void submitDialog(String text) throws DatabaseException, SQLException {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            if(!newType.equals(user.getUserTypeU()) || projectsToAdd.size() != 0 || projectsToDelete.size() != 0) {
                UserTypeChangeTransaction.changeProjects(user, projectsToAdd, projectsToDelete);
            }
            UsersAdministrationItemController.getInstance().closeAllDialogs(stages);
        }
        else {
            alert.close();
        }

    }

    /**
     * Zobrazenie potvrdzovacieho dialogu pri role bezny uzivatel
     * @param text text v dialogu
     * @throws DatabaseException
     * @throws SQLException
     */
    private void submitDialogNoChanges(String text) throws DatabaseException, SQLException {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            UsersAdministrationItemController.getInstance().closeAllDialogs(stages);
        }
        else {
            alert.close();
        }

    }

    /**
     * Zisti, ci je zoznam aktualnych projektov iny, ako ten, ktory chceme pridelit uzivatelovi
     * @return true alebo false
     * @throws SQLException
     */
    private boolean sameToAdd() throws SQLException {
        List<Project> userProjects = AdministrationService.getAdministrationService().findProjectsByAdmin(user.getEmail());
        List<Project> notTheSameProjects = userProjects.stream()
                .filter(e -> !projectsToAdd.contains(e.getProjectNumber()))
                .collect(Collectors.toList());
        return notTheSameProjects.size() == 0;
    }


}
