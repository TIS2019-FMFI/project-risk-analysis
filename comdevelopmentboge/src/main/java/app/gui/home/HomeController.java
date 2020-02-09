package app.gui.home;

import app.App;
import app.config.SignedUser;
import app.db.ProjectReminder;
import app.db.RegistrationRequest;
import app.db.User;
import app.exception.DatabaseException;
import app.gui.MyAlert;
import app.service.LogService;
import app.service.ProjectReminderService;
import app.service.RegistrationRequestService;
import app.service.UserService;
import app.transactions.Registration;
import app.transactions.ReminderTransaction;
import com.jfoenix.controls.JFXListView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jfree.chart.plot.AbstractPieLabelDistributor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HomeController {

    private Map<String, List<ProjectReminder>> reminders;
    private List<RegistrationRequest> requests;
    private List<String> hidden_projects_codes = new ArrayList<>();
    private int rows;
    private int cols = 3;

    private static HomeController homeController = new HomeController();

    public static HomeController getHomeController() {
        return homeController;
    }

    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXListView projectList;
    @FXML
    private Label welcome;
    @FXML
    private Label project_hidden;
    @FXML
    private Button project_button;


    public void initialize() throws IOException {

        setWelcome();
        switch (SignedUser.getUser().getUserTypeU()) {
            case USER:
                initUser();
                break;
            case CENTRAL_ADMIN:
                initCentralAdmin();
                break;
            case PROJECT_ADMIN:
                initProjectAdmin();
                break;
        }


    }

    private void initCentralAdmin() throws IOException {
        setColsByAppSize();
        getRequests();
        getReminders();
        setNotificationScene();
        setProjectListEvent();
    }

    private void initProjectAdmin() throws IOException {
        setColsByAppSize();
        getReminders();
        setNotificationScene();
        setProjectListEvent();

    }

    private void initUser() {
        project_button.setVisible(false);
        scrollPane.setVisible(false);
        projectList.setVisible(false);

    }

    private void setProjectListEvent() {
        projectList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                int id = projectList.getSelectionModel().getSelectedIndex();
                System.out.println(id);
                if (id != -1) {
                    String code = ((VBox) projectList.getSelectionModel().getSelectedItem()).getId();
                    try {
                        projectList.getItems().remove(id);
                        showRemindersByProjectNumber(code);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void setColsByAppSize() {
        cols = Math.floorDiv((int) App.getScene().getWidth() - 200, 270);
    }

    private FXMLLoader getFXMLLoader(String fxml) {
        return new FXMLLoader(HomeController.class.getResource(fxml + ".fxml"));
    }

    private Parent loadFXMLreminder(String fxml, ProjectReminder reminder, int col, int row) throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(fxml);
        Parent root = fxmlLoader.load();
        ReminderController controller = fxmlLoader.getController();
        controller.set(reminder, col, row, this);
        return root;
    }

    private Parent loadFXMLrequest(String fxml, RegistrationRequest request, int col, int row) throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(fxml);
        Parent root = fxmlLoader.load();
        RequestController controller = fxmlLoader.getController();
        controller.set(request, col, row, this);
        return root;
    }

    private void setWelcome() {
        welcome.setText("Vitaj " + SignedUser.getUser().getFullName() + "!");
    }

    private void setNotificationScene() throws IOException {
        rearrangeGridPane();
    }

    private void getReminders() {

        try {
            ReminderTransaction.loadReminders();
            List<ProjectReminder> reminders0;
            if (SignedUser.getUser().getUserTypeU() == User.USERTYPE.CENTRAL_ADMIN) {
                //ak je centralny admin tak sa ukazu vsetky notifikacie
                reminders0 = ProjectReminderService.getInstance().getActiveReminders();
            } else {
                //ak je projektovy tak len tie co su v ramci jeho projektu
                reminders0 = ProjectReminderService.getInstance().getActiveRemindersByUser(SignedUser.getUser().getId());
            }
            reminders = reminders0.stream().collect(Collectors.groupingBy(ProjectReminder::getProjectNumber));


        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void getRequests() {
        try {
            requests = RegistrationRequestService.getInstance().findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showAllHiddenReminders() throws IOException {
        for (String code : hidden_projects_codes) {
            showReminders(reminders.get(code));

        }
        hidden_projects_codes.clear();
        projectList.getItems().clear();
        rearrangeGridPane();

    }

    private void showReminders(List<ProjectReminder> reminders1) {
        for (ProjectReminder reminder : reminders1) {
            if (reminder.getIsMinimized()) {
                reminder.setIsMinimized(false);
                String count = Integer.toString(Integer.valueOf(project_hidden.getText()) - 1);
                project_hidden.setText(count);
            }
        }
    }

    @FXML
    public void showRemindersByProjectNumber(String code) throws IOException {
        showReminders(reminders.get(code));
        hidden_projects_codes.remove(code);
        rearrangeGridPane();
    }


    public void approveRequest(RegistrationRequest request, int col, int row) throws IOException {
        deleteReminder(col, row);
        requests.remove(request);
        rearrangeGridPane();
        try {
            Registration.approveRegistrationRequest(request);
            String fullname = UserService.getInstance().findUserById(request.getUser_id()).getFullName();
            LogService.createLog("Schválenie žiadosti o registráciu používateľa " + fullname);
            MyAlert.showSuccess("Žiadosť užívateľa bola úspešne schválená");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

    }

    public void declineRequest(RegistrationRequest request, int col, int row) throws IOException {
        if(MyAlert.showConfirmationDialog("Naozaj chcete zrušiť túto žiadosť o registráciu ? \n Táto akcia je nevratná")) {
            deleteReminder(col, row);
            requests.remove(request);
            rearrangeGridPane();
            try {
                String fullname = UserService.getInstance().findUserById(request.getUser_id()).getFullName();
                Registration.declineRegistrationRequest(request);
                LogService.createLog("Zrušenie žiadosti o registráciu používateľa " + fullname);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }

    }

    public void minimizeReminder(ProjectReminder reminder, int col, int row) throws IOException {


        String count = Integer.toString(Integer.valueOf(project_hidden.getText()) + 1);
        project_hidden.setText(count);
        deleteReminder(col, row);

        reminder.setIsMinimized(true);
        if (!hidden_projects_codes.contains(reminder.getProjectNumber())) {
            hidden_projects_codes.add(reminder.getProjectNumber());
            projectList.getItems().addAll(createProjectItem(reminder.getProjectNumber()));
        }

        rearrangeGridPane();
    }

    private VBox createProjectItem(String projectNumber) throws IOException {
        FXMLLoader loader = getFXMLLoader("project_item");
        VBox box = loader.load();
        box.setId(projectNumber);
        loader.<HiddenProjectItemController>getController().set(projectNumber, this);
        return box;
    }


    public void closeReminder(ProjectReminder reminder, int col, int row) throws IOException {
        if(MyAlert.showConfirmationDialog("Naozaj chcete zrušiť túto notifikáciu ? \n Táto akcia je nevratná")) {
            deleteReminder(col, row);
            reminder.setIsClosed(true);
            rearrangeGridPane();
            try {
                reminder.update();
                LogService.createLog("Uzavretie notifikácie týkajúcej sa projektu: " + reminder.getProjectNumber());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }



    }

    private void deleteReminder(int col, int row) throws IOException {
        Node node = getNodeFromGridPane(col, row);
        if (node != null) {
            gridPane.getChildren().remove(node);
        }
    }

    private Node getNodeFromGridPane(int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    private void rearrangeGridPane() throws IOException {
        gridPane.getChildren().clear();
        int row = 0;
        int col = 0;
        if (requests != null) {
            for (RegistrationRequest request : requests) {
                AnchorPane req = new AnchorPane(loadFXMLrequest("request", request, col, row));
                gridPane.add(req, col, row);
                col += 1;
                if (col >= cols) {
                    col = 0;
                    row += 1;
                }
            }
        }

        if (reminders != null) {
            for (List<ProjectReminder> reminders : reminders.values()) {
                for (ProjectReminder reminder : reminders) {
                    if (!reminder.getIsClosed() && !reminder.getIsMinimized()) {
                        AnchorPane notification = new AnchorPane(loadFXMLreminder("reminder", reminder, col, row));
                        gridPane.add(notification, col, row);

                        col += 1;
                        if (col >= cols) {
                            col = 0;
                            row += 1;
                        }
                    }
                }


            }
        }


    }
}
