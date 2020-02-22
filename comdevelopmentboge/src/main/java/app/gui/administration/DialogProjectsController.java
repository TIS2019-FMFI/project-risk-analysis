package app.gui.administration;

import app.App;
import app.db.Project;
import app.db.User;
import app.exception.DatabaseException;
import app.service.AdministrationService;
import app.service.ProjectService;
import app.transactions.UserTypeChangeTransaction;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DialogProjectsController {

    /**
     * Getter a setter instancie dialog
     */
    private static DialogProjectsController instance;
    public static DialogProjectsController getInstance(){return instance;}


    /**
     * projectsListView - graficky komponent, ktory zobrazuje zoznam projektov, ktorych je uzivatel adminom
     * projectsListView - graficky komponent, ktory zobrazuje zoznam projektov, ktorych uz uzivatel nebude adminom
     * projectNumber - graficky komponent, ktory zobrazuje cislo projektu, ktory chceme pridelit uzivatelovi
     */

    @FXML private JFXListView<Pane> projectsListView;
    @FXML private JFXListView<Pane> deleteProjectsListView;
    @FXML ComboBox projectsBox;

    @FXML Text projectsUser;


    /**
     * user - užívateľ, ktorého rolu chceme zmeniť
     * projects - projekty, ktorých je užívateľ adminom
     * stages - otvorené dialógové okná
     */
    private User user;
    private List<Project> projects;
    private List<String> projectsToAdd = new ArrayList<>();
    private List<String> projectsToDelete = new ArrayList<>();
    private ArrayList<String> freeProjects;
    List<Stage> stages;

    /**
     * Nastavenie grafickeho komentu - ziskanie projektov, ktorých adminom je uzivatel
     * @param user - objekt typu uzivatel(user), ktoreho rolu chceme zmenit
     * @throws SQLException chyba pri ziskavani dat z databazy
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    public void init(User user) throws SQLException, IOException {
        this.user = user;
        instance = this;
        projectsUser.setText("Projekty používateľa " + user.getFullName());

        this.projects = AdministrationService.getAdministrationService().findProjectsByAdmin(user.getEmail());
        for(Project project : projects) {
            projectsListView.getItems().add(setPane(project.getProjectNumber()));
        }
        projectsListView.setPrefWidth(App.getScene().getWidth());
        projectsListView.setPrefHeight(App.getScene().getHeight() - 80);

    }

    /**
     * Nastavenie dialogoveho okna pre zmenu projektov uzivatela
     * @param stages aktualne otvorene dialogove okna
     * @param user pouzivatel, ktoremu chceme zmenit rolu
     * @throws SQLException chyba pri ziskavani dat z databazy
     * @throws IOException chyba v grafickom komponente
     */
    public void setProjectAdminDialog(List<Stage> stages, User user) throws SQLException, IOException {
        this.stages = stages;
        this.user = user;
        instance = this;
        this.freeProjects = ProjectService.getProjectService().getFreeProjectsNums();
        this.projects = AdministrationService.getAdministrationService().findProjectsByAdmin(user.getEmail());
        for(Project project : projects) {
            deleteProjectsListView.getItems().add(setProjectAdminPane(project.getProjectNumber()));
        }
        deleteProjectsListView.setPrefWidth(App.getScene().getWidth());
        deleteProjectsListView.setPrefHeight(App.getScene().getHeight() - 80);

        projectsBox.getItems().addAll(FXCollections.observableArrayList(ProjectService.getProjectService().getFreeProjectsNums()));
        projectsBox.getItems().add(0,null);
    }

    /**
     * Nastavenie polozky zoznamu projektov, teda konkretneho projektu
     * @param projectNumber - cislo projektu
     * @return prvok pane s cislom projektu
     * @throws IOException chyba v grafickom komponente
     */
    private Pane setPane(String projectNumber) throws IOException {
        FXMLLoader loader = loadFXML("projectAdmin-show-projects-item");
        Pane pane = loader.load();
        loader.<DialogProjectsItemController>getController().setProjectNumber(projectNumber);
        pane.setId(projectNumber);
        return pane;
    }

    /**
     * Nastavenie polozky na odstranenie projektu zo zoznamu projektov
     * @param projectNumber - cislo projektu
     * @return pane s cislom projekt
     * @throws IOException  chyba v grafickom komponente
     */
    private Pane setProjectAdminPane(String projectNumber) throws IOException {
        FXMLLoader loader = loadFXML("projects-administration-dialog-item");
        Pane pane = loader.load();
        loader.<DialogProjectsItemController>getController().setProjectNumber(projectNumber);
        pane.setId(projectNumber);
        return pane;
    }

    /**
     * Nacitanie fxml suboru
     * @param fxml subor
     * @return nacitany subor
     * @throws IOException chyba v grafickom komponente
     */
    private FXMLLoader loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(UsersAdministrationBoardController.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
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
     * Zatvorenie otvorenych dialogovych okien
     * @param event
     */
    @FXML
    private void closeProjects(MouseEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    /**
     * Zobrazenie potvrdzovacieho diaogu
     * @param event
     * @throws SQLException chyba pri ziskavani dat z databazy
     * @throws IOException chyba v grafickom komponente
     * @throws DatabaseException chyba v databaze
     */
    @FXML
    private void submitAll(MouseEvent event) throws SQLException, IOException, DatabaseException {
        showAdministrationConfirmDialog();
    }

    /**
     * Nastavenie potvrdzovacieho dialógového okna
     * @throws IOException chyba v grafickom komponente
     * @throws DatabaseException chyba v databaze
     * @throws SQLException chyba pri ziskavani dat z databazy
     */
    private void showAdministrationConfirmDialog() throws IOException, DatabaseException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("administration-user-confirm-dialog.fxml"));
        Parent parent = fxmlLoader.load();
        DialogConfirmController dialogController = fxmlLoader.getController();

        Scene scene = new Scene(parent, 300, 400);
        Stage stage = new Stage();
        stages.add(stage);
        UsersAdministrationItemController.getInstance().onCloseHandler(stages.get(stages.size()-1), this.stages);
        dialogController.setDialog(stages, user, projects, projectsToAdd, projectsToDelete);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }


    /**
     * Pridanie projektu uzivatelovi
     * @param event
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    public void addProject(MouseEvent event) throws IOException {
        if(projectsBox.getValue() != null) {
            String projectNum = projectsBox.getValue().toString();
            projectsToAdd.add(projectNum);
            Project project = ProjectService.getProjectService().findProjectByNum(projectNum);
            projects.add(project);
            if(projectsToDelete.contains(projectNum)) {
                projectsToDelete.remove(projectNum);
            }
            freeProjects.remove(projectNum);
            reloadDeleteProjectsListView();
            reloadAddProjects();
        }
    }

    /**
     * Vymaze projekte
     * @param projectNum cislo projektu
     * @throws IOException chyba v grafickom komponente
     * @throws SQLException chyba pri ziskavani dat z databazy
     */
    public void deleteProject(String projectNum) throws IOException, SQLException {
        projects = projects.stream()
                .filter(i -> !i.getProjectNumber().equals(projectNum))
                .collect(Collectors.toList());
        reloadDeleteProjectsListView();
        projectsToDelete.add(projectNum);
        if(projectsToAdd.contains(projectNum)) {
            projectsToAdd.remove(projectNum);
        }
        freeProjects.add(projectNum);
        reloadAddProjects();
    }

    /**
     * Opatovne nacitanie projektov, ktore nemaju ziadneho admina v grafickom komponente ComboBox
     */
    public void reloadAddProjects() {
        projectsBox.getItems().clear();
        projectsBox.getItems().addAll(FXCollections.observableArrayList(freeProjects));
        projectsBox.getItems().add(0,null);
    }

    private void reloadDeleteProjectsListView() throws IOException {
        deleteProjectsListView.getItems().clear();
        for(Project project : projects) {
            deleteProjectsListView.getItems().add(setProjectAdminPane(project.getProjectNumber()));
        }
    }

}
