package app.gui.home;

import app.App;
import app.db.FEMReminder;
import app.db.ProjectReminder;
import app.db.Reminder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeController {

    private List<Reminder> reminders = new ArrayList<>();
    private int rows;
    private int cols = 2;

    private static HomeController homeController = new HomeController();
    public static HomeController getHomeController() { return homeController;}

    @FXML private GridPane gridPane;
    @FXML private Label welcome;
    @FXML private Label FEM_hidden;
    @FXML private Label project_hidden;


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
        int i = 0;
        int j = 0;
        for(Reminder reminder : reminders) {
            AnchorPane notification = new AnchorPane(loadFXMLreminder("reminder", reminder, j, i, this));
            gridPane.add(notification, j, i);
            j += 1;
            if(j > cols) {
                j = 0;
                i += 1;
            }
        }
    }

    private void generatedString() {
        String text = "Prekročenie povoleného limitu simulácií (5) pre súčiastku 373 100 013 015 za obdobie 3 týždňov";
        Reminder r1 = new FEMReminder(1, text);

        String project = "Projekt CH-060020";
        text = "Náklady na projekte sa blížia k stanovenej hranici 20 000 €";
        Reminder r2 = new ProjectReminder(2, text, project);

        reminders.add(r1);
        reminders.add(r2);
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
        int i = 0;
        int j = 0;
        for(Reminder reminder : reminders) {
            if(!reminder.getIsClosed() && !reminder.getIsMinimized()) {
                AnchorPane notification = new AnchorPane(loadFXMLreminder("reminder", reminder, j, i, this));
                gridPane.add(notification, j, i);

                j += 1;
                if(j > cols) {
                    j = 0;
                    i += 1;
                }
            }

        }
    }
}
