package app.gui.home;

import app.db.ProjectReminder;
import app.db.RegistrationRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RequestController {

    private HomeController homeController;
    private RegistrationRequest request;
    private int col;
    private int row;
    @FXML
    private Label name;
    @FXML private TextArea text;

    public RequestController() {

    }

    public void set(RegistrationRequest request, int col, int row, HomeController homeCon) {


        name.setText("Žiadosť o registráciu");

        text.setText(request.getText());

        this.col = col;
        this.row = row;
        this.request = request;
        this.homeController = homeCon;
    }


    @FXML
    private void decline(MouseEvent event) throws IOException {
       homeController.declineRequest(request,col,row);
    }

    @FXML
    private void approve(MouseEvent event) throws IOException {
        homeController.approveRequest(request,col,row);
    }
}
