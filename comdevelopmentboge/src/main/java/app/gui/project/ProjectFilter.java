package app.gui.project;

import app.gui.TabController;
import javafx.fxml.FXML;

import java.io.IOException;

public class ProjectFilter {

    @FXML
    public void onBackPressed() throws IOException {
        TabController.getInstance().selectProjectTab();
    }
}
