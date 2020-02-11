package app.gui.project;

import app.db.Customer;
import app.service.CustomerService;
import app.service.ProjectService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


public class ProjectListFilterController {

    @FXML
    private TextField projectName;

    @FXML
    private TextField projectNumber;

    @FXML
    private TextField customer;

    @FXML
    private ImageView refresh;

    /**
     * initialize all components in filter bar, sets functions to handle events
     */
    @FXML
    public void initialize(){


        projectName.setOnKeyReleased(keyEvent ->
        {
            filterByName(projectName.getText());
        });

        projectNumber.setOnKeyReleased(keyEvent ->
        {
            filterByNumber(projectNumber.getText());
        });


        customer.setOnKeyReleased(keyEvent ->
        {
            filterByCustomer(customer.getText());
        });

    }

    /**
     * filter project list by name
     * @param name
     */
    private void filterByName(String name){
        ProjectListFilter.getProjectListFilter().setProjectName(name);
    }

    /**
     * filter project list by number
     * @param number
     */
    private void filterByNumber(String number){
        ProjectListFilter.getProjectListFilter().setProjectNumber(number);
    }

    /**
     * filter project list by customer
     * @param customerId
     */
    private void filterByCustomer(String customerId){
        ProjectListFilter.getProjectListFilter().setCustomer(customerId);
    }

    /**
     * refreshuje hodnoty filtrov a zobrazi zoznam projektov bez pouzitia filtrov
     */
    @FXML
    public void refreshFilter(){
        projectName.setText("");
        projectNumber.setText("");
        customer.setText("");
        refresh.requestFocus();

        ProjectListFilter.getProjectListFilter().restartValues();
        ProjectTabController.getInstance().reloadList();
    }


}
