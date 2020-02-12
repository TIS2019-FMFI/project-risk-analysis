package app.gui.home;

import app.App;
import app.config.SignedUser;
import app.db.ProjectReminder;
import app.db.RegistrationRequest;
import app.db.User;
import app.exception.DatabaseException;
import app.exception.GmailMessagingException;
import app.exception.MyException;
import app.gui.MyAlert;
import app.service.LogService;
import app.service.ProjectReminderService;
import app.service.RegistrationRequestService;
import app.service.UserService;
import app.transactions.Registration;
import app.transactions.ReminderTransaction;
import com.jfoenix.controls.JFXListView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jfree.chart.plot.AbstractPieLabelDistributor;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HomeController {

    /**
     * Obsahuje notifikacie rozdelene do mapy podla cisla projektu
     */
    private Map<String, List<ProjectReminder>> reminders;

    /**
     * Obsahuje ID notifikacii
     * Sluzi pre identifikaciu uz zobrazenych notifikacii, ked sa nacitavaju nove
     */
    private List<Integer> reminderIDs = new ArrayList<>();

    /**
     * Obsahuje ziadosti o registraciu
     */
    private List<RegistrationRequest> requests;

    /**
     * Obsahuje kody projektov, ktore su skryte v zozname
     */
    private List<String> hidden_projects_codes = new ArrayList<>();

    /**
     * Pocet riadkov GRIDPANE, v ktorom su ulozene notifikacie a ziadosti o registraciu
     */
    private int rows;

    /**
     * Pocet stlpcov GRIDPANE, v ktorom su ulozene notifikacie a ziadosti o registraciu
     * Po nacitani stranky sa prepocita sirka a nastavi sa podla toho pocet stlpcov
     */
    private int cols = 3;

    /**
     * Instancia triedy
     */
    private static HomeController homeController = new HomeController();

    public static HomeController getHomeController() {
        return homeController;
    }

    /**
     * Kontainer predstavujuci GRID
     * Obsahuje notifikacie a ziadosti zobrazene na hlavnej stranke
     */
    @FXML
    private GridPane gridPane;

    /**
     * Kontainer ktory obsahuje gridpane
     * Sluzi pre scrollovanie notifikaciami
     */
    @FXML
    private ScrollPane scrollPane;

    /**
     * Obsahuje graficke prvky, s nazvom projektu ktoreho notifikacie bola minimalizovane
     * */
    @FXML
    private JFXListView projectList;

    /**
     * Graficky prvok ktory obsahuje uvitaciu spravu
     */
    @FXML
    private Label welcome;

    /**
     * Graficky prvok ktory obsahuje pocet skrytych notifikacii
     */
    @FXML
    private Label project_hidden;

    /**
     * Tlacidlo ktore po kliknuti opat zobrazi vsetky skryte notifikacie
     */
    @FXML
    private Button project_button;

    /**
     * Tlacidlo ktor po kliknuti znovu nacita udaje z databazy
     * a prepocita notifikacie ktore zobrazi na stranke
     */
    @FXML
    private Button refresh;


    /**
     * Inicializovanie podla role prihlaseneho uzivatela
     * Nastavi hodnoty grafickym prvkom, prepocita notifikacie z databazy,
     * posle mailove notifikacie, zobrazi notifikacie a ziadosti na stranke
     * @throws IOException chyba v grafickom komponente
     */
    public void initialize() throws IOException {

        setWelcome();
        loadReminders();

        switch (SignedUser.getUser().getUserTypeU()) {
            case USER:
                initUser();
                break;
            case CENTRAL_ADMIN:
                initCentralAdmin();
                break;
            case PROJECT_ADMIN:
                initProjectAdmin();
                break;
        }
        sendReminders();


    }

    /**
     * Inicializovanie stranky pre rolu centralneho admina
     * Zobrazia sa vsetky notifikacie ako aj ziadosti o registraciu
     * @throws IOException chyba v grafickom komponente
     */
    private void initCentralAdmin() throws IOException {
        setColsByAppSize();
        getRequests();
        getReminders();
        setNotificationScene();
        setProjectListEvent();
    }

    /**
     * Inicializovanie stranky pre rolu projektoveho admina
     * Zobrazia sa len notifikacie tykajuce sa jeho projektov
     * @throws IOException chyba v grafickom komponente
     */
    private void initProjectAdmin() throws IOException {
        setColsByAppSize();
        getReminders();
        setNotificationScene();
        setProjectListEvent();

    }

    /**
     * Inicializovanie pre bezneho uzivatela
     * Skryje kontajner pre zobrazenie notifikacii a dalsie graficke prvky
     * suvisiace s nimi
     */
    private void initUser() {
        project_button.setVisible(false);
        scrollPane.setVisible(false);
        projectList.setVisible(false);
        refresh.setVisible(false);

    }

    /**
     * Po kliknuti na zoznam projektov znovu zobrazi notifikacie tykajuce sa daneho projektu
     */
    private void setProjectListEvent() {
        projectList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                int id = projectList.getSelectionModel().getSelectedIndex();
                System.out.println(id);
                if (id != -1) {
                    String code = ((VBox) projectList.getSelectionModel().getSelectedItem()).getId();
                    try {
                        projectList.getItems().remove(id);
                        showRemindersByProjectNumber(code);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    /**
     * Znovu nacita notifikacie z databazy a prida ich do clenskych premennych a zobrazi
     * ich na stranke
     * @param event
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    private void refreshReminders(MouseEvent event) throws IOException {
        try {
            loadReminders();
            List<ProjectReminder> newReminders = getRemindersFromDB();

            for (ProjectReminder rem : newReminders) {
                if (!reminderIDs.contains(rem.getId())) {
                    //ak tato notifikacia tam este nie je tak ju prida
                    if (!reminders.containsKey(rem.getProjectNumber())) {
                        reminders.put(rem.getProjectNumber(), new ArrayList<>());
                    }

                    reminders.get(rem.getProjectNumber()).add(rem);

                    reminderIDs.add(rem.getId());
                }
            }
            rearrangeGridPane();
        } catch (SQLException e) {
            MyAlert.showError(DatabaseException.ERROR);
            e.printStackTrace();
        }

    }

    /**
     * Nastavi pocet stlpcov podla sirky okna
     */
    private void setColsByAppSize() {
        cols = Math.floorDiv((int) App.getScene().getWidth() - 200, 270);
    }

    /**
     * Vrati FXMLLoader podla nazvu FXML suboru
     * @param fxml nazov FXML suboru reprezentujuci danu entitu
     * @return
     */
    private FXMLLoader getFXMLLoader(String fxml) {
        return new FXMLLoader(HomeController.class.getResource(fxml + ".fxml"));
    }

    /**
     * Zobrazi graficku podobu notifikacie a nastavi tuto notifikaciu
     * ReminderControlleru
     * @param fxml nazov FXML reprezentujuci danu notifikaciu
     * @param reminder instancia notifikacie
     * @param col stlpec umiestnenia notifikacie v GRIDPANE
     * @param row riadok umiestnenia notifikacie v GRIDPANE
     * @return graficky objekt notifikacie
     * @throws IOException chyba v grafickom komponente
     */
    private Parent loadFXMLreminder(String fxml, ProjectReminder reminder, int col, int row) throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(fxml);
        Parent root = fxmlLoader.load();
        ReminderController controller = fxmlLoader.getController();
        controller.set(reminder, col, row, this);
        return root;
    }

    /**
     * Zobrazi graficku podobu ziadosti o registraciu a nastavi tuto notifikaciu
     * RRequestControlleru
     * @param fxml nazov FXML reprezentujuci danu ziadost
     * @param request instancia ziadosti
     * @param col stlpec umiestnenia ziadosti v GRIDPANE
     * @param row riadok umiestnenia ziadosti v GRIDPANE
     * @return graficky objekt ziadosti
     * @throws IOException chyba v grafickom komponente
     */
    private Parent loadFXMLrequest(String fxml, RegistrationRequest request, int col, int row) throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(fxml);
        Parent root = fxmlLoader.load();
        RequestController controller = fxmlLoader.getController();
        controller.set(request, col, row, this);
        return root;
    }

    /**
     * Nastavi meno uzivatela pre uvitaciu spravu do grafickeho komponentu
     */
    private void setWelcome() {
        welcome.setText("Vitaj " + SignedUser.getUser().getName() + "!");
    }

    /**
     * Rozdeli a zobrazi notifikacie a ziadosti na hlavnu stranku
     * @throws IOException chyba v grafickom komponente
     */
    private void setNotificationScene() throws IOException {
        rearrangeGridPane();
    }

    /**
     * Vytvori nove vlakno, ktore ma za ulohu ziskat z databazy notifikacie
     * a posle ich na prislusne maily
     */
    private void sendReminders() {
        Runnable r = new Runnable() {
            public void run() {
                try {
                    ReminderTransaction.sentReminders();
                } catch (MyException e ) {
                    MyAlert.showError(e.getMessage());
                    e.printStackTrace();
                } catch (SQLException e) {
                    MyAlert.showError(DatabaseException.ERROR);
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }

    /**
     * Prepocita udaje z projektovej a SAP tabulky a na zaklade toho
     * vlozi notifikacie do databazy
     */
    private void loadReminders()  {
        try {
            ReminderTransaction.loadReminders();
        } catch (SQLException e) {
            MyAlert.showError(DatabaseException.ERROR);
            e.printStackTrace();
        } catch (DatabaseException e) {
            MyAlert.showError(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Nacita notifikacie z databazy
     * @return
     * @throws SQLException chyba v grafickom komponente
     */
    private List<ProjectReminder> getRemindersFromDB() throws SQLException {
        List<ProjectReminder> reminders0;
        if (SignedUser.getUser().getUserTypeU() == User.USERTYPE.CENTRAL_ADMIN) {
            //ak je centralny admin tak sa ukazu vsetky notifikacie
            reminders0 = ProjectReminderService.getInstance().getActiveReminders();
        } else {
            //ak je projektovy tak len tie co su v ramci jeho projektu
            reminders0 = ProjectReminderService.getInstance().getActiveRemindersByUser(SignedUser.getUser().getId());
        }
        return reminders0;
    }

    /**
     * Nastavenie zoznamu vsetkych notifikacii, podla ID projektu
     */
    private void getReminders() {

        try {
            List<ProjectReminder> reminders0 = getRemindersFromDB();
            reminders = reminders0.stream().collect(Collectors.groupingBy(ProjectReminder::getProjectNumber));
            reminderIDs = reminders0.stream().map(rem -> rem.getId()).collect(Collectors.toList());

        } catch (SQLException e) {
            MyAlert.showError(DatabaseException.ERROR);
            e.printStackTrace();
        }

    }

    /**
     * Nastavenie zoznamu vsetkych ziadosti o registraciu
     */
    private void getRequests() {
        try {
            requests = RegistrationRequestService.getInstance().findAll();
        } catch (SQLException e) {
            MyAlert.showError(DatabaseException.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Schovanie vsetkych notifikacii z hlavnej obrazovky
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    public void showAllHiddenReminders() throws IOException {
        for (String code : hidden_projects_codes) {
            showReminders(reminders.get(code));

        }
        hidden_projects_codes.clear();
        projectList.getItems().clear();
        rearrangeGridPane();

    }

    /**
     * Zobrazenie vsetkych minimalizovanych notifikacii na hlavnej obrazovke
     * @param reminders1
     */
    private void showReminders(List<ProjectReminder> reminders1) {
        for (ProjectReminder reminder : reminders1) {
            if (reminder.getIsMinimized()) {
                reminder.setIsMinimized(false);
                String count = Integer.toString(Integer.valueOf(project_hidden.getText()) - 1);
                project_hidden.setText(count);
            }
        }
    }

    /**
     * Zobrazenie notifikacii podla projektoveho cisla
     * @param code cislo projektu
     * @throws IOException chyba v grafickom komponente
     */
    @FXML
    public void showRemindersByProjectNumber(String code) throws IOException {
        showReminders(reminders.get(code));
        hidden_projects_codes.remove(code);
        rearrangeGridPane();
    }


    /**
     * Schvalenie registracnej ziadosti
     * @param request objekt typu RegistrationReguest
     * @param col stlpec, v ktorom je ziadost
     * @param row riadok, v ktorom je ziadost
     * @throws IOException chbyba v grafickom komponente
     */
    public void approveRequest(RegistrationRequest request, int col, int row) throws IOException {
        deleteReminder(col, row);
        requests.remove(request);
        rearrangeGridPane();
        try {
            Registration.approveRegistrationRequest(request);
            String fullname = UserService.getInstance().findUserById(request.getUser_id()).getFullName();
            LogService.createLog("Schválenie žiadosti o registráciu používateľa " + fullname);
            MyAlert.showSuccess("Žiadosť užívateľa bola úspešne schválená");
        } catch (SQLException e) {
            MyAlert.showError(DatabaseException.ERROR);
            e.printStackTrace();
        } catch (DatabaseException e) {
            MyAlert.showError(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Zamietnutie registracnej ziadosti
     * @param request objekt typu RegistrationRequest
     * @param col stlpec, v ktorom je ziadost
     * @param row riadok, v ktorom je ziadost
     * @throws IOException chyba v grafickom komponente
     */
    public void declineRequest(RegistrationRequest request, int col, int row) throws IOException {
        if(MyAlert.showConfirmationDialog("Naozaj chcete zrušiť túto žiadosť o registráciu ? \n Táto akcia je nevratná")) {
            deleteReminder(col, row);
            requests.remove(request);
            rearrangeGridPane();
            try {
                String fullname = UserService.getInstance().findUserById(request.getUser_id()).getFullName();
                Registration.declineRegistrationRequest(request);
                LogService.createLog("Zrušenie žiadosti o registráciu používateľa " + fullname);
            } catch (SQLException e) {
                MyAlert.showError(DatabaseException.ERROR);
                e.printStackTrace();
            } catch (DatabaseException e) {
                MyAlert.showError(e.getMessage());
                e.printStackTrace();
            }
        }

    }

    /**
     * Minimalizovanie notifikacie
     * @param reminder objekt typu ProjectReminder
     * @param col stlpec, v ktorom je notifikacia
     * @param row riadok, v ktorom je notifikacia
     * @throws IOException chyba v grafickom komponente
     */
    public void minimizeReminder(ProjectReminder reminder, int col, int row) throws IOException {


        String count = Integer.toString(Integer.valueOf(project_hidden.getText()) + 1);
        project_hidden.setText(count);
        deleteReminder(col, row);

        reminder.setIsMinimized(true);
        if (!hidden_projects_codes.contains(reminder.getProjectNumber())) {
            hidden_projects_codes.add(reminder.getProjectNumber());
            projectList.getItems().addAll(createProjectItem(reminder.getProjectNumber()));
        }

        rearrangeGridPane();
    }

    /**
     * Vytvorenie obdlznika s cislom projektu po minimalizovani notifikacie k projektu
     * @param projectNumber projektove cislo
     * @return graficky komponent box
     * @throws IOException chyba v grafickom komponente
     */
    private VBox createProjectItem(String projectNumber) throws IOException {
        FXMLLoader loader = getFXMLLoader("project_item");
        VBox box = loader.load();
        box.setId(projectNumber);
        loader.<HiddenProjectItemController>getController().set(projectNumber, this);
        return box;
    }


    /**
     * Zatvorenie/vymazanie notifikacie k projektu z hlavnej obrazovky
     * @param reminder objekt typu ProjectReminder
     * @param col stlpec, v ktorom je notifikacia
     * @param row riadok, v ktorom je notifikacia
     * @throws IOException
     */
    public void closeReminder(ProjectReminder reminder, int col, int row) throws IOException {
        if(MyAlert.showConfirmationDialog("Naozaj chcete zrušiť túto notifikáciu ? \n Táto akcia je nevratná")) {
            deleteReminder(col, row);
            reminder.setIsClosed(true);
            rearrangeGridPane();
            try {
                reminder.update();
                LogService.createLog("Uzavretie notifikácie týkajúcej sa projektu: " + reminder.getProjectNumber());
            } catch (SQLException e) {
                MyAlert.showError(DatabaseException.ERROR);
                e.printStackTrace();
            }
        }



    }

    /**
     * Vymazanie reminderu z hlavnej obrazovky (z tabulky notifikacii)
     * @param col stlpec, v ktorom je reminder
     * @param row riadok, v ktorom je reminder
     */
    private void deleteReminder(int col, int row) {
        Node node = getNodeFromGridPane(col, row);
        if (node != null) {
            gridPane.getChildren().remove(node);
        }
    }

    /**
     * Ziskanie grafickeho komponentu - notifikacie z tabulky
     * @param col stlpec, v ktorom je graficky komponent notifikacie
     * @param row riadok, v ktorom je graficky komponent notifikacie
     * @return graficky komponent node
     */
    private Node getNodeFromGridPane(int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Opatovne nacitanie grafickeho komponentu tabulka
     * @throws IOException chyba v grafickom komponente
     */
    private void rearrangeGridPane() throws IOException {
        gridPane.getChildren().clear();
        int row = 0;
        int col = 0;
        if (requests != null) {
            for (RegistrationRequest request : requests) {
                AnchorPane req = new AnchorPane(loadFXMLrequest("request", request, col, row));
                gridPane.add(req, col, row);
                col += 1;
                if (col >= cols) {
                    col = 0;
                    row += 1;
                }
            }
        }

        if (reminders != null) {
            for (List<ProjectReminder> reminders : reminders.values()) {
                for (ProjectReminder reminder : reminders) {
                    if (!reminder.getIsClosed() && !reminder.getIsMinimized()) {
                        AnchorPane notification = new AnchorPane(loadFXMLreminder("reminder", reminder, col, row));
                        gridPane.add(notification, col, row);

                        col += 1;
                        if (col >= cols) {
                            col = 0;
                            row += 1;
                        }
                    }
                }


            }
        }


    }
}
