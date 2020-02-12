package app.gui.home;

import app.db.ProjectReminder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * Trieda reprezentujuca policko skrytej notifikacie projektu
 */
public class HiddenProjectItemController {
    /**
     * Controller hlavnej stranky
     */
    private HomeController homeController;

    /**
     * Unikatny kod projektu
     */
    private String code;
    /**
     * Graficky prvok zobrazujuci meno projektu
     */
    @FXML
    private Label project_name;

    public HiddenProjectItemController(){}

    /**
     * Nastavi policku kod projektu a controller hlavnej triedy
     * @param code
     * @param homeCon
     */
    public void set(String code, HomeController homeCon) {

        this.code = code;
        project_name.setText(code);
    }



}

