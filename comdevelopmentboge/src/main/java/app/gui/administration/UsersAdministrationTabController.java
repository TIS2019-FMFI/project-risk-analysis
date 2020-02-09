package app.gui.administration;


import app.gui.TabController;

import java.io.IOException;

public class UsersAdministrationTabController {

    /**
     * Getter a setter inštancie
     */
    private static UsersAdministrationTabController instance = new UsersAdministrationTabController();
    public static UsersAdministrationTabController getInstance(){return instance;}


    /**
     * Zobrazenie tabuľky pre administráciu užívateľov
     * @throws IOException
     */
    public void init() throws IOException {
        TabController.getInstance().selectUsersAdministration();
    }

}
