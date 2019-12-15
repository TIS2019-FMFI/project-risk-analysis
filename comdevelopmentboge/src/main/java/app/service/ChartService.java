package app.service;

import app.config.DbContext;
import app.gui.graph.Period;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class ChartService {

    private static ChartService chartService = new ChartService();
    public static ChartService getChartService(){ return  chartService;}

    public LinkedHashMap<Period, BigDecimal> getRDCostsData(String projectCode) {
        LinkedHashMap<Period, BigDecimal> costs = new LinkedHashMap<>();
        String sql = "select Periode as month, Jahr as year, sum(Wert) from sap where projectNr=? and Objektbezeichnung='Research & Development Trnava' and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' and KostenartenBez='Entwicklungskosten' group by Periode, Jahr order by Jahr, Periode";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){
            preparedStatement.setString(1,projectCode);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){

                String month = rs.getString(1);
                String year = rs.getString(2);
                BigDecimal amount = rs.getBigDecimal(3);
                costs.put(new Period(month, year), amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return costs;
    }

    public LinkedHashMap<Period, BigDecimal> getCostsData(String projectCode) {
        LinkedHashMap<Period, BigDecimal> costs = new LinkedHashMap<>();
        String sql = "select Periode as month, Jahr as year, sum(Wert) from sap where projectNr=? and Objektbezeichnung='Research & Development Trnava' and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' and KostenartenBez not like '%Entwicklungskosten%' group by Periode, Jahr order by Jahr, Periode";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){
            preparedStatement.setString(1,projectCode);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                String month = rs.getString(1);
                String year = rs.getString(2);
                BigDecimal amount = rs.getBigDecimal(3);
                costs.put(new Period(month, year), amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return costs;
    }

    public LinkedHashMap<Period, BigDecimal> getPrototypeCosts(String projectCode) {
        LinkedHashMap<Period, BigDecimal> costs = new LinkedHashMap<>();
        String sql = "select Periode as month, Jahr as year, sum(Wert) from sap where projectNr=? and Objektbezeichnung='Samples + Revenues Trnava' and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' group by Periode, Jahr order by Jahr, Periode";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){
            preparedStatement.setString(1,projectCode);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                String month = rs.getString(1);
                String year = rs.getString(2);
                BigDecimal amount = rs.getBigDecimal(3);
                costs.put(new Period(month, year), amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return costs;
    }

    public LinkedHashMap<Period, BigDecimal> getPrototypeRevenues(String projectCode) {
        LinkedHashMap<Period, BigDecimal> costs = new LinkedHashMap<>();
        String sql = "select Periode as month, Jahr as year, sum(Wert) from sap where projectNr=? and Objektbezeichnung='Samples + Revenues Trnava' and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' and Wert<=0 group by Periode, Jahr order by Jahr, Periode";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){
            preparedStatement.setString(1,projectCode);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                String month = rs.getString(1);
                String year = rs.getString(2);
                BigDecimal amount = rs.getBigDecimal(3);
                amount = amount.multiply(BigDecimal.valueOf(-1));
                costs.put(new Period(month, year), amount );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return costs;
    }
}
