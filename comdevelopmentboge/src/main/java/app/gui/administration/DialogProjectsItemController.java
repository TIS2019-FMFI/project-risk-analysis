package app.gui.administration;

import app.gui.MyAlert;
import app.transactions.UserTypeChangeTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;


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
    private void deleteProject(MouseEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Chceš odstrániť projekt " +  projectNumberLbl.getText()+ "?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            UserTypeChangeTransaction.deleteProject(projectNumberLbl.getText());
        }
        if (alert.getResult() == ButtonType.CANCEL) {
            alert.close();
        }
    }
}
