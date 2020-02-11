package app.gui.project;

import app.App;
import app.db.Project;
import app.db.ProjectCosts;
import app.gui.TabController;
import app.service.ProjectCostsService;
import app.service.ProjectService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import com.jfoenix.controls.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class ProjectTabController{

    private static ProjectTabController instance;
    public static ProjectTabController getInstance(){return instance;}

    @FXML
    private JFXListView<Pane> projectListView;

    @FXML
    public void initialize() {

        instance = this;

        reloadList();

        projectListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Pane pane = projectListView.getSelectionModel().getSelectedItem();
                if(pane != null) {
                    try {
                        showProjectDetails(pane.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        projectListView.setPrefWidth(App.getScene().getWidth());
        projectListView.setPrefHeight(App.getScene().getHeight() - 80);
    }

    /**
     * sets tab content to be project details
     * @param projectDef
     * @throws IOException
     */
    private void showProjectDetails(String projectDef) throws IOException {
        TabController.getInstance().selectProjectDetailsTab(projectDef);
    }

    /**
     * set UI component for each project in list
     * @param project
     * @param projectsToBeWarned
     * @return
     * @throws IOException
     */
    private Pane setProject(Project project, HashSet<String> projectsToBeWarned) throws IOException {
        FXMLLoader loader = loadFXML("project-list-item");
        Pane pane = loader.load();
        pane.setId(project.getProjectNumber());

        Text projectNumberTxt = (Text) pane.lookup("#projectNumber");
        projectNumberTxt.setText(project.getProjectNumber());

        Text projectNameTxt = (Text) pane.lookup("#projectName");
        String projectName = project.getProjectName();
        projectNameTxt.setText((projectName==null)?"":projectName);

        Image image = new Image(App.class.getResourceAsStream("/app/images/warning.png"));
        if(!projectsToBeWarned.contains(project.getProjectNumber())){
            image = null;
        }

        ImageView warningIcon = (ImageView) pane.lookup("#warningIcon");
        if(image != null){
            warningIcon.setImage(image);
        }
        return pane;
    }

    private FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ProjectTabController.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    /**
     * reload project list according to filter
     */
    public void reloadList(){
        ArrayList<Project> projects= ProjectService.getProjectService().findProjectsByCriteria();
        HashSet<String> projectsToBeWarned = getProjectsToBeWarned();

        projectListView.getItems().clear();
        for(Project project: projects) {
            try {
                projectListView.getItems().add(setProject(project, projectsToBeWarned));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get projects that need to be warned by showing warn sign in list item UI component
     * @return
     */
    private HashSet<String> getProjectsToBeWarned(){
        HashSet<ProjectCosts> costsList = new HashSet<>();
        try {
            costsList.addAll(ProjectCostsService.getInstance().getDDCosts());
            costsList.addAll(ProjectCostsService.getInstance().getPrototypeCosts());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //get numbers of projects that have exceeded the limit in DD and prototype costs respec.

        HashSet<String> projectsToBeWarned = new HashSet<>();
        for(ProjectCosts projectCosts : costsList){
            projectsToBeWarned.add(projectCosts.getProjectNumber());
        }

        return projectsToBeWarned;
    }
}
