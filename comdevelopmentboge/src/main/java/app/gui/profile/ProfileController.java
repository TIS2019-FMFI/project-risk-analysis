package app.gui.profile;

import app.App;
import app.gui.TabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class ProfileController {

    /**
     * Getter a setter inštancie profilu
     */
    private static ProfileController instance = new ProfileController();
    public static ProfileController getInstance(){return instance;}


    /**
     * Nastavenie scény
     * @throws IOException
     */
    public void init() throws IOException {
        TabController.getInstance().selectProfile();
    }




}
