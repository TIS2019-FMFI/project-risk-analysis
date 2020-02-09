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
     * Getter a setter inštancie tabuľka užívateľov
     */
    private static UsersAdministrationBoardController instance;
    public static UsersAdministrationBoardController getInstance(){return instance;}

    /**
     * userListView - grafický komponent, ktorý zobrazuje zoznam užívateľov
     */
    @FXML
    private JFXListView<Pane> userListView;


    /**
     * Nastavenie zoznamu užívateľov
     * @throws IOException
     * @throws SQLException
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
     * Nastavenie položky zoznamu užívateľov - nerozkliknutá položka
     * @param user - konkrétny užívateľ
     * @return
     * @throws IOException
     */
    private Pane setUser(User user) throws IOException {
        FXMLLoader loader = loadFXML("users-administration-item");
        Pane pane = loader.load();
        pane.setId(user.getFullName());
        loader.<UsersAdministrationItemController>getController().setUser(user);
        return pane;
    }

    /**
     * Nastavenie položky zoznamu užívateľov - rozkliknutá položka
     * @param fullName - celé meno používateľa
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private Pane setUserDetail(String fullName) throws IOException, SQLException {
        User user = UserService.getInstance().findUserByFullName(fullName);
        FXMLLoader loader;
        if(user.getUserType().equals("PROJECT_ADMIN")) {
            loader = loadFXML("users-administration-item-click-PA");
        }
        else {
            loader = loadFXML("users-administration-item-click");
        }
        Pane pane = loader.load();
        loader.<UsersAdministrationItemController>getController().setInfo(fullName);
        return pane;
    }


    /**
     * Načítanie fxml súboru
     * @param fxml
     * @return
     * @throws IOException
     */
    private FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UsersAdministrationBoardController.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }


    /**
     * Zatvorenie aktuálneho dialógového okna
     * @param event
     */
    @FXML
    private void close(MouseEvent event) throws IOException {
        TabController.getInstance().closeUsersAdministration();
    }

    /**
     *
     * Zobrazenie detailu užívateľa - po kliknutí
     * @param fullname - celé meno užívateľa
     * @throws SQLException
     * @throws IOException
     */
    public void showDetail(String fullname) throws SQLException, IOException {
        List<User> users = UserService.getInstance().findAllUsers();
        userListView.getItems().clear();
        for(User user : users) {
            if(user.getFullName().equals(fullname)) {
                userListView.getItems().add(setUserDetail(user.getFullName()));
            }
            else {
                userListView.getItems().add(setUser(user));
            }
        }
    }



}
