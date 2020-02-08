package app.gui.administration;

import app.App;
import app.db.Project;
import app.db.User;
import app.exception.DatabaseException;
import app.service.AdministrationService;
import app.transactions.UserTypeChangeTransaction;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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
     * thisStage - aktuálne otvorené dialógové okno
     */
    private User user;
    private List<Project> projectsToSave;
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
    public void setDialog(List<Stage> stages, User user, List<Project> projects) throws IOException {
        instance = this;
        this.stages = stages;
        this.user = user;
        this.projectsToSave = projects;
        this.newType = User.USERTYPE.PROJECT_ADMIN;
        fullName.setText(user.getFullName());
        userType.setText("Projektový admin");

        for(Project project : projectsToSave) {
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
        stages.get(0).close();
    }

    /**
     * Spustenie transakcie na zmenu roly a projektov užívateľa
     * @param event
     * @throws SQLException
     * @throws DatabaseException
     */
    @FXML
    private void submit(MouseEvent event) throws SQLException, DatabaseException {
        if(!newType.equals(user.getUserType())) {
            UserTypeChangeTransaction.changeProjects(user, difference());
        }
        UsersAdministrationItemController.getInstance().closeAllDialogs(stages);
    }

    /**
     * Projekty, ktoré chceme vymazať, teda admin projektu nebude užívateľ
     * @return
     * @throws DatabaseException
     * @throws SQLException
     */
    private List<Project> difference() throws DatabaseException, SQLException {
        List<Project> allProjects = AdministrationService.getAdministrationService().findProjectsByAdmin(user.getFullName());

        Set<String> projectNums = projectsToSave.stream()
                .map(Project::getProjectNumber)
                .collect(Collectors.toSet());
        List<Project> projectsToDelete = allProjects.stream()
                .filter(project -> !projectNums.contains(project.getProjectNumber()))
                .collect(Collectors.toList());

        return projectsToDelete;
    }

}
