package app.transactions;

import app.config.DbContext;
import app.db.SAP;
import app.importer.ExcelRow;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;



public class ImportSAPTransaction {


    public static void importSAP(ArrayList<ExcelRow> sap) throws SQLException {

        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        try {
            SAP temp = new SAP();
            temp.insertFromFile(sap);
            DbContext.getConnection().commit();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }
}

