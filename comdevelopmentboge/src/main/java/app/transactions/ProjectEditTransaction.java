package app.transactions;

import app.config.DbContext;
import app.db.Customer;
import app.db.Project;
import app.service.CustomerService;
import app.service.LogService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class ProjectEditTransaction {

    public static void editProject(Project project, HashMap<String, String> changes) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        try{

        Customer customer = null;
        if(project.getCustomerName()!=null){
            customer = CustomerService.getCustomerService().findCustomerByName(project.getCustomerName());

            //if customer is null, create one
            if(customer == null) {
                customer = new Customer();
                customer.setName(project.getCustomerName());
                int id= customer.insert();
                customer.setId(id);
            }

        }

        if(customer!=null){
            project.setCustomerId(customer.getId());
        }
        //update project
        project.update();

        String text = " updated values: ";

        for(String columnName: changes.keySet()){
            text += columnName + "=" + changes.get(columnName) + ", ";
        }

        text = text.substring(0, text.length() - 2) + " of project: " + project.getProjectNumber();

        //log changes
        LogService.createLog(text);

        } catch (SQLException e){
            DbContext.getConnection().rollback();
            throw e;

        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }

    }
}
