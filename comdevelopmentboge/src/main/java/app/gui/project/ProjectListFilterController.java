package app.gui.project;

import app.db.Customer;
import app.service.CustomerService;
import app.service.ProjectService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;


public class ProjectListFilterController {

    @FXML
    private ComboBox projectName;

    @FXML
    private ComboBox projectNumber;

    @FXML
    private ComboBox<Customer> customer;

    @FXML
    public void initialize(){

        projectName.getItems().addAll(FXCollections.observableArrayList(ProjectService.getProjectService().getAllProjectNames()));
        projectName.getItems().add(0,null);
        projectNumber.getItems().addAll(FXCollections.observableArrayList(ProjectService.getProjectService().getAllProjectNumbers()));
        projectNumber.getItems().add(0,null);

        customer.getItems().addAll(FXCollections.observableArrayList(CustomerService.getCustomerService().getAllCustomer()));
        customer.getItems().add(0,null);
        EventHandler<ActionEvent> projectNameChosenEvent =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        filterByName((projectName.getValue()==null)?null:projectName.getValue().toString());
                    }

            };
        EventHandler<ActionEvent> projectNumberChosenEvent =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        filterByNumber((projectNumber.getValue()==null)?null:projectNumber.getValue().toString());
                    }
            };
        EventHandler<ActionEvent> customerChosenEvent =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        filterByCustomer((customer.getValue()==null)?0:customer.getValue().getId());
                    }
                };

        projectName.setOnAction(projectNameChosenEvent);
        projectNumber.setOnAction(projectNumberChosenEvent);
        customer.setOnAction(customerChosenEvent);
    }

    private void filterByName(String name){
        ProjectListFilter.getProjectListFilter().setProjectName(name);
    }

    private void filterByNumber(String number){
        ProjectListFilter.getProjectListFilter().setProjectNumber(number);
    }

    private void filterByCustomer(int customerId){
        ProjectListFilter.getProjectListFilter().setCustomer(customerId);
    }


}
