package app.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class MyAlert {

    /**
     * Zobrazenie dialógu po zistení chyby - typ error
     * @param text
     */
    public static void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    /**
     * Zobrazenie dialógu - typ confirmation
     * @param text
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
     * Zobrazenie dialógu - typ warning
     * @param text
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
     * @param text
     * @return
     */
    public static String showInputDialog(String text) {
        TextInputDialog td = new TextInputDialog("");
        td.setHeaderText(text);
        td.showAndWait();
        return td.getEditor().getText();
    }



}
