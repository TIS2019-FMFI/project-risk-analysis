package app.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class MyAlert {

    /**
     * Zobrazenie dialogu po zisteni chyby - typ error
     * @param text text v dialogu
     */
    public static void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    /**
     * Zobrazenie dialogu - typ confirmation
     * @param text text v dialogu
     */
    public static void showSuccess(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text, ButtonType.OK);
        ImageView imageView = new ImageView("app/images/conf.png");
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        alert.getDialogPane().setGraphic(imageView);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    /**
     * Zobrazenie dialogu - typ warning
     * @param text text v dialogu
     */
    public static void showWarning(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }

    }

    /**
     * Zobrazenie dialógu - typ input, teda s textovým vstupom
     * @param text - text dialógu
     * @return
     */
    public static String showInputDialog(String text) {
        TextInputDialog td = new TextInputDialog("");
        td.setHeaderText(text);
        td.showAndWait();
        return td.getEditor().getText();
    }

    /**
     * Zobrazenie dialogu pre potvrdenie vykonanej akcie
     * @param text
     * @return true ak uzivatel potvrdi dialog inak false
     */
    public static Boolean showConfirmationDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            return true;
        }
        alert.close();
        return false;

    }



}
