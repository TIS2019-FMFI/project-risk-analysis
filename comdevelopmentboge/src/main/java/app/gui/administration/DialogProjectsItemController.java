package app.gui.administration;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;


public class DialogProjectsItemController {

    /**
     * projectNumberLbl - graficky komponent, ktory zobrazuje projektove cislo
     */
    @FXML private Label projectNumberLbl;

    /**
     * Nastavenie grafickeho komponentu - cislo projektu
     * @param projectNumber - cislo projektu
     */
    public void setProjectNumber(String projectNumber) {
        projectNumberLbl.setText(projectNumber);
    }

    /**
     * Vymazanie projektu - uzivatel uz nie je adminom projektu
     * @param event
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    private void deleteProject(MouseEvent event) throws SQLException, IOException {
        DialogProjectsController.getInstance().deleteProject(projectNumberLbl.getText());
    }
}
