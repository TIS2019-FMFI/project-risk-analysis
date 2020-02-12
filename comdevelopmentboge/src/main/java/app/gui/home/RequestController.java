package app.gui.home;

import app.db.ProjectReminder;
import app.db.RegistrationRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RequestController {

    /**
     * homeController - objekt typu homeController, kontroler hlavnej obrazovky
     * request - registracna ziadost
     * col - stlpec, v ktorom je registracna ziadost
     * row - riadok, v ktorom je registracna ziadost
     * name - nazov ziadosti
     * text - text ziadosti
     */
    private HomeController homeController;
    private RegistrationRequest request;
    private int col;
    private int row;
    @FXML
    private Label name;
    @FXML private TextArea text;

    /**
     * Nastavenie objektu RequestController, kontroler registracnej ziadosti
     */
    public RequestController() {

    }

    /**
     * Nastavenie registracnej ziadosti
     * @param request - ziadost o registraciu
     * @param col stlpec, v ktorom je ziadost
     * @param row riadok, v ktorom je ziadost
     * @param homeCon kontroler hlavnej obrazovky
     */
    public void set(RegistrationRequest request, int col, int row, HomeController homeCon) {

        name.setText("Žiadosť o registráciu");
        text.setText(request.getText());

        this.col = col;
        this.row = row;
        this.request = request;
        this.homeController = homeCon;
    }

    /**
     * Zamietnutie registracnej ziadosti
     * @param event
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    private void decline(MouseEvent event) throws IOException {
       homeController.declineRequest(request,col,row);
    }

    /**
     * Schvalenie registracnej ziadosti
     * @param event
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    private void approve(MouseEvent event) throws IOException {
        homeController.approveRequest(request,col,row);
    }
}
