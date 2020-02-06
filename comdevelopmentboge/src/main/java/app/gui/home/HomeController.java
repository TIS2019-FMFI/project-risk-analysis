package app.gui.home;

import app.App;
import app.db.FEMReminder;
import app.db.ProjectReminder;
import app.db.Reminder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeController {

    private List<Reminder> reminders = new ArrayList<>();
    private int rows;
    private int cols = 3;

    private static HomeController homeController = new HomeController();
    public static HomeController getHomeController() { return homeController;}

    @FXML private GridPane gridPane;
    @FXML private Label welcome;
    @FXML private Label FEM_hidden;
    @FXML private Label project_hidden;
    @FXML private Button project_button;


    public void initialize() throws IOException {
        //setScene();
        generatedString();
        setWelcome();
        setNotificationScene(reminders);

    }

    public static void setScene() throws IOException {
        Parent parent = loadFXML("admin-main");
        App.setRoot(parent);
    }


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static Parent loadFXMLreminder(String fxml, Reminder reminder, int col, int row, HomeController homeCon) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeController.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        ReminderController controller = fxmlLoader.getController();
        controller.set(reminder, col, row, homeCon);
        return root;
    }


    private void setNotificationScene(List<Reminder> reminders) throws IOException {
        int row = 0;
        int col = 0;
        for(Reminder reminder : reminders) {
            AnchorPane notification = new AnchorPane(loadFXMLreminder("reminder", reminder, col, row, this));
            gridPane.add(notification, col, row);
            col += 1;
            if(col > cols) {
                col = 0;
                row += 1;
            }
        }
    }

    private void generatedString() {

        for(int i = 0;i < 10;i++) {
            String project = "Projekt" + i;
            String text = "Náklady na projekte sa blížia k stanovenej hranici 20 000 €";
            Reminder r = new ProjectReminder(2, text, project);
            reminders.add(r);

        }

    }
    @FXML
    public void showProjectReminders() throws IOException {
        //prechadzam minimalizovane remindre a zobrazujem ich
        for(Reminder reminder : reminders) {
            if (reminder.getIsMinimized() && reminder instanceof ProjectReminder) {
                reminder.setIsMinimized(false);
            }
        }
        project_hidden.setText("0");
        rearrangeGridPane();
    }

    private void setWelcome() {
        // welcome.setText("Vitaj " + SignedUser.getSignedUser().getName() + "!");
    }

    public void minimizeReminder(Reminder reminder, int col, int row) throws IOException {
        if(reminder instanceof FEMReminder) {
            System.out.println(FEM_hidden);
            String count = Integer.toString(Integer.valueOf(FEM_hidden.getText()) + 1);
            FEM_hidden.setText(count);
        }
        else if(reminder instanceof ProjectReminder) {
            String count = Integer.toString(Integer.valueOf(project_hidden.getText()) + 1);
            project_hidden.setText(count);
        }
        deleteReminder(col, row);

        reminders.get(reminders.indexOf(reminder)).setIsMinimized(true);
        rearrangeGridPane();
    }

    public void closeReminder(Reminder reminder, int col, int row) throws IOException {
        deleteReminder(col, row);
        reminders.get(reminders.indexOf(reminder)).setIsClosed(true);
        rearrangeGridPane();
    }


    private void deleteReminder(int col, int row) throws IOException {
        Node node = getNodeFromGridPane(col, row);
        if(node != null) {
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
        for(Reminder reminder : reminders) {
            if(!reminder.getIsClosed() && !reminder.getIsMinimized()) {
                AnchorPane notification = new AnchorPane(loadFXMLreminder("reminder", reminder, col, row, this));
                gridPane.add(notification, col, row);

                col += 1;
                if(col > cols) {
                    col = 0;
                    row += 1;
                }
            }

        }
    }
}
