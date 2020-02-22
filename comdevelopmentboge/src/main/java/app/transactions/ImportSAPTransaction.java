package app.transactions;

import app.config.DbContext;
import app.db.Project;
import app.db.ProjectReminder;
import app.db.SAP;
import app.importer.ExcelRow;
import app.service.ProjectService;
import app.service.SAPService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;



public class ImportSAPTransaction {

    /**
     * Vytvori transakciu na vlozenie dat do databazy
     * @param sap
     * @throws SQLException
     */
    public static void importSAP(ArrayList<ExcelRow> sap) throws SQLException {

        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        try {

            //delete all records from table sap
            SAPService.getSapService().deleteAllRecords();

            SAP temp = new SAP();
            temp.insertFromFile(sap);


            ProjectService.getProjectService().importProjects();

            DbContext.getConnection().commit();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }
}

