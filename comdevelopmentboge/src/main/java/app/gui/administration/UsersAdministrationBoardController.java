package app.gui.administration;

import app.App;
import app.db.User;
import app.gui.TabController;
import app.service.UserService;
import com.jfoenix.controls.JFXListView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UsersAdministrationBoardController {


    /**
     * Getter a setter instancie tabulka uzivatelov
     */
    private static UsersAdministrationBoardController instance;
    public static UsersAdministrationBoardController getInstance(){return instance;}

    /**
     * userListView - graficky komponent, ktory zobrazuje zoznam uzivatelov
     */
    @FXML
    private JFXListView<Pane> userListView;


    /**
     * Nastavenie zoznamu uzivatelov
     * @throws IOException chyba v grafickom komponente
     * @throws SQLException chyba pri ziskavani dat z databazy
     */
    @FXML
    public void initialize() throws IOException, SQLException {
        instance = this;
        List<User> users = UserService.getInstance().findAllUsers();
        for(User user : users) {
            userListView.getItems().add(setUser(user));
        }

        userListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Pane pane = userListView.getSelectionModel().getSelectedItem();
                if(pane != null) {
                    try {
                        showDetail(pane.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        userListView.setPrefWidth(App.getScene().getWidth());
        userListView.setPrefHeight(App.getScene().getHeight() - 80);
    }

    /**
     * Nastavenie polozky zoznamu uzivatelov - nerozkliknuta polozka
     * @param user - konkretny uzivatel
     * @return
     * @throws IOException
     */
    private Pane setUser(User user) throws IOException {
        FXMLLoader loader = loadFXML("users-administration-item");
        Pane pane = loader.load();
        pane.setId(user.getEmail());
        loader.<UsersAdministrationItemController>getController().setUser(user);
        return pane;
    }

    /**
     * Nastavenie polozky zoznamu uzivatelov - rozkliknuta polozka
     * @param email - email pouzivatela
     * @return pane s celym menom uzivatela
     * @throws IOException chyba v grafickom komponente
     * @throws SQLException chyba pri ziskavani dat z databazy
     */
    private Pane setUserDetail(String email) throws IOException, SQLException {
        User user = UserService.getInstance().findUserByEmail(email);
        FXMLLoader loader;
        if(user.getUserType().equals("PROJECT_ADMIN")) {
            loader = loadFXML("users-administration-item-click-PA");
        }
        else {
            loader = loadFXML("users-administration-item-click");
        }
        Pane pane = loader.load();
        loader.<UsersAdministrationItemController>getController().setInfo(email);
        return pane;
    }


    /**
     * Nacitanie fxml suboru
     * @param fxml subor
     * @return nacitany subor
     * @throws IOException chyba v grafickom komponente
     */
    private FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UsersAdministrationBoardController.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }


    /**
     * Zatvorenie aktualneho dialogoveho okna
     * @param event
     */
    @FXML
    void close(MouseEvent event) throws IOException {
        TabController.getInstance().closeUsersAdministration();
    }

    /**
     *
     * Zobrazenie detailu uzivatela - po kliknuti
     * @param email - cele meno uzivatela
     * @throws SQLException chyba pri ziskavani dat z databazy
     * @throws IOException chyba v grafickom komponente
     */
    public void showDetail(String email) throws SQLException, IOException {
        List<User> users = UserService.getInstance().findAllUsers();
        userListView.getItems().clear();
        for(User user : users) {
            if(user.getEmail().equals(email)) {
                userListView.getItems().add(setUserDetail(user.getEmail()));
            }
            else {
                userListView.getItems().add(setUser(user));
            }
        }
    }

}
