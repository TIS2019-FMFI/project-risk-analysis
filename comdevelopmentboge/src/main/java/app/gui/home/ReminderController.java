package app.gui.home;

import app.db.ProjectReminder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;


public class ReminderController {

    /**
     * homeController - objekt typu homeController, graficky kontroler hlavnej obrazovky
     * reminder - konkretna notifikacia
     * col - stlpec, v ktorom je notifikacia
     * row - riadok, v ktorom je notifikacia
     * name - nazov, teda projektove cislo nofifikacie
     * text - text notifikacie
     */
    private HomeController homeController;
    private ProjectReminder reminder;
    private int col;
    private int row;
    @FXML private Label name;
    @FXML private TextArea text;

    /**
     * Nastavenie objektu ReminderController
     */
    public ReminderController() {

    }

    /**
     * Nastavenie notifikacie
     * @param reminder - objekt typu ProjectReminder
     * @param col stlpec, v ktorom je notifikacia
     * @param row riadok, v ktorom je notifikacia
     * @param homeCon controller hlavnej obrazovky
     */
    public void set(ProjectReminder reminder, int col, int row, HomeController homeCon) {


        name.setText(reminder.getProjectNumber());

        text.setText(reminder.getText());

        this.col = col;
        this.row = row;
        this.reminder = reminder;
        this.homeController = homeCon;
    }


    /**
     * Zatvorenie notifikacie
     * @param event
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    private void close(MouseEvent event) throws IOException {
        homeController.closeReminder(reminder, col, row);
    }

    /**
     * Minimalizovanie notifikacie
     * @param event
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    private void minimize(MouseEvent event) throws IOException {
        homeController.minimizeReminder(reminder, col, row);
    }

}
