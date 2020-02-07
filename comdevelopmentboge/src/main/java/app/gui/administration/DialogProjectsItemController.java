package app.gui.administration;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;


public class DialogProjectsItemController {

    /**
     * projectNumberLbl - grafický komponent, ktorý zobrazuje projektové číslo
     */
    @FXML private Label projectNumberLbl;

    /**
     * Nastavenie grafického komponentu - číslo projektu
     * @param projectNumber
     */
    public void setProjectNumber(String projectNumber) {
        projectNumberLbl.setText(projectNumber);
    }

    /**
     * Vymazanie projektu - užívateľ už nie je adminom projektu
     * @param event
     * @throws IOException
     */
    @FXML
    private void deleteProject(MouseEvent event) throws IOException {
        DialogProjectsController.getInstance().deleteProject(projectNumberLbl.getText());
    }
}
