package app.gui.home;

import app.db.ProjectReminder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class HiddenProjectItemController {
    private HomeController homeController;
    private String code;
    @FXML
    private Label project_name;

    public HiddenProjectItemController(){}

    public void set(String code, HomeController homeCon) {

        this.code = code;
        project_name.setText(code);
    }



}

