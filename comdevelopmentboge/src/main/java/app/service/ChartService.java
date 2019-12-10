package app.service;

import app.config.DbContext;
import app.gui.graph.RDCosts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChartService {

    private static ChartService chartService = new ChartService();
    public static ChartService getChartService(){ return  chartService;}

    public RDCosts getRDCostsData(String projectCode) {
        RDCosts costs = new RDCosts();
        String sql = "select Periode as month, Jahr as year, sum(Wert) from sap where projectNr=? and Objektbezeichnung='Research & Development Trnava' and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' group by Periode, Jahr order by Jahr, Periode";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){
            preparedStatement.setString(1,projectCode);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                costs.addRecord(rs.getString(1), rs.getString(2), rs.getBigDecimal(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return costs;
    }

}
