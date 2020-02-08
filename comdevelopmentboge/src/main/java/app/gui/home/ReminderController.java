package app.gui.home;

import app.db.ProjectReminder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;


public class ReminderController {

    private HomeController homeController;
    private ProjectReminder reminder;
    private int col;
    private int row;
    @FXML private Label name;
    @FXML private TextArea text;

    public ReminderController() {

    }

    public void set(ProjectReminder reminder, int col, int row, HomeController homeCon) {


        name.setText(reminder.getProjectNumber());

        text.setText(reminder.getText());

        this.col = col;
        this.row = row;
        this.reminder = reminder;
        this.homeController = homeCon;
    }


    @FXML
    private void close(MouseEvent event) throws IOException {
        homeController.closeReminder(reminder, col, row);
    }

    @FXML
    private void minimize(MouseEvent event) throws IOException {
        homeController.minimizeReminder(reminder, col, row);
    }

}
