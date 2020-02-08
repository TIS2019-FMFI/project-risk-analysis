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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class DialogProjectsController {

    /**
     * Getter a setter inštancie dialóg
     */
    private static DialogProjectsController instance;
    public static DialogProjectsController getInstance(){return instance;}


    /**
     * projectsListView - grafický komponent, ktorý zobrazuje zoznam projektov, ktorých je užívateľ adminom
     * projectsListView - grafický komponent, ktorý zobrazuje zoznam projektov, ktorých už užívateľ nebude adminom
     * projectNumber - grafický komponent, ktorý zobrazuje číslo projektu, ktorý chceme prideliť užívateľovi
     */

    @FXML private JFXListView<Pane> projectsListView;
    @FXML private JFXListView<Pane> deleteProjectsListView;
    @FXML ComboBox projectsBox;


    /**
     * user - užívateľ, ktorého rolu chceme zmeniť
     * projects - projekty, ktorých je užívateľ adminom
     * thisStage - aktuálne otvorené dialógové okno
     */
    private User user;
    private List<Project> projects;
    List<Stage> stages;

    /**
     * Nastavenie grafického komentu - získanie projektov, ktorých adminom je užívateľ
     * @param user - objekt typu užívateľ(user), ktorého rolu chceme zmeniť
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    public void init(User user) throws SQLException, IOException {
        this.user = user;
        instance = this;
        this.projects = AdministrationService.getAdministrationService().findProjectsByAdmin(user.getFullName());
        for(Project project : projects) {
            projectsListView.getItems().add(setPane(project.getProjectNumber()));
        }
        projectsListView.setPrefWidth(App.getScene().getWidth());
        projectsListView.setPrefHeight(App.getScene().getHeight() - 80);

    }

    /**
     * Nastavenie dialógového okna pre zmenu projektov užívateľa
     * @param stages
     * @param user
     * @throws DatabaseException
     * @throws SQLException
     * @throws IOException
     */
    public void setProjectAdminDialog(List<Stage> stages, User user) throws DatabaseException, SQLException, IOException {
        this.stages = stages;
        this.user = user;
        instance = this;
        this.projects = AdministrationService.getAdministrationService().findProjectsByAdmin(user.getFullName());
        for(Project project : projects) {
            deleteProjectsListView.getItems().add(setProjectAdminPane(project.getProjectNumber()));
        }
        deleteProjectsListView.setPrefWidth(App.getScene().getWidth());
        deleteProjectsListView.setPrefHeight(App.getScene().getHeight() - 80);

        projectsBox.getItems().addAll(FXCollections.observableArrayList(ProjectService.getProjectService().getFreeProjectsNums()));
        projectsBox.getItems().add(0,null);
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
     * Nastavenie položky na odstránenie projektu zo zoznamu projektov
     * @param projectNumber - číslo projektu
     * @return
     * @throws IOException
     */
    private Pane setProjectAdminPane(String projectNumber) throws IOException {
        FXMLLoader loader = loadFXML("projects-administration-dialog-item");
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
    private FXMLLoader loadFXML(String fxml) {
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


    @FXML
    private void closeProjects(MouseEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    /**
     * Zobrazenie potvrdzovacieho dialógu
     * @param event
     * @throws SQLException
     * @throws IOException
     * @throws DatabaseException
     */
    @FXML
    private void submitAll(MouseEvent event) throws SQLException, IOException, DatabaseException {
        showAdministrationConfirmDialog();
    }

    /**
     * Nastavenie potvrdzovacieho dialógového okna
     * @throws IOException
     * @throws DatabaseException
     * @throws SQLException
     */
    private void showAdministrationConfirmDialog() throws IOException, DatabaseException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("administration-user-confirm-dialog.fxml"));
        Parent parent = fxmlLoader.load();
        DialogConfirmController dialogController = fxmlLoader.getController();

        Scene scene = new Scene(parent, 300, 400);
        Stage stage = new Stage();
        stages.add(stage);
        UsersAdministrationItemController.getInstance().onCloseHandler(stages.get(stages.size()-1), this.stages);
        dialogController.setDialog(stages, user, projects);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }




    @FXML
    public void addProject(MouseEvent event) throws DatabaseException, SQLException {
        if(projectsBox.getValue() != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Chceš prideliť projekt " +  projectsBox.getValue().toString() +
                    " užívateľovi " + user.getFullName() + "?", ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                UserTypeChangeTransaction.addProject(user, projectsBox.getValue().toString());
            }
            if (alert.getResult() == ButtonType.CANCEL) {
                alert.close();
            }
        }
    }

    public void reloadAddProjects() {
        projectsBox.getItems().clear();
        projectsBox.getItems().addAll(FXCollections.observableArrayList(ProjectService.getProjectService().getFreeProjectsNums()));
        projectsBox.getItems().add(0,null);
    }

    public void reloadProjectsDelete() throws SQLException, IOException {
        deleteProjectsListView.getItems().clear();
        this.projects = AdministrationService.getAdministrationService().findProjectsByAdmin(user.getFullName());
        for(Project project : projects) {
            deleteProjectsListView.getItems().add(setProjectAdminPane(project.getProjectNumber()));
        }
    }



}
