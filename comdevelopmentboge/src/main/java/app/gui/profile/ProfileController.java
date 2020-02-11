package app.gui.profile;

import app.App;
import app.gui.TabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class ProfileController {

    /**
     * Getter a setter instancie profilu
     */
    private static ProfileController instance = new ProfileController();
    public static ProfileController getInstance(){return instance;}


    /**
     * Nastavenie sceny
     * @throws IOException chyba v grafickom komponente
     */
    public void init() throws IOException {
        TabController.getInstance().selectProfile();
    }




}
