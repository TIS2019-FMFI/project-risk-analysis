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

    public LinkedHashMap<Period, BigDecimal> getRDCostsData(String projectCode, java.sql.Date fromDate, java.sql.Date toDate) {
        String sql = "select Periode as month, Jahr as year, sum(WertKWahr) from sap where Projektdef=? and BuchDatum between ? and ? and Objektbezeichnung='Research & Development Trnava' and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' " +
                "and KostenartenBez='Entwicklungskosten' group by Periode, Jahr order by BuchDatum";
        return getCosts(projectCode, fromDate, toDate, sql);
    }

    public LinkedHashMap<Period, BigDecimal> getCostsData(String projectCode, java.sql.Date fromDate, java.sql.Date toDate) {
        String sql = "select Periode as month, Jahr as year, sum(WertKWahr) from sap where Projektdef=? and BuchDatum between ? and ? and" +
                " (Objektbezeichnung='Research & Development Trnava' or ((Objektbezeichnung='Samples + Revenues Trnava' or Objektbezeichnung='Molds and Tool costs + Revenues Trnava') and WertKWahr>0)) " +
                " and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' group by Periode, Jahr order by BuchDatum";
        return getCosts(projectCode, fromDate, toDate, sql);
    }

    public LinkedHashMap<Period, BigDecimal> getPrototypeCosts(String projectCode, java.sql.Date fromDate, java.sql.Date toDate)  {
        String sql = "select Periode as month, Jahr as year, sum(WertKWahr) from sap where Projektdef=? and BuchDatum between ? and ? and (Objektbezeichnung='Samples + Revenues Trnava' or Objektbezeichnung='Molds and Tool costs + Revenues Trnava')" +
                " and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' and WertKWahr>0 group by Periode, Jahr order by BuchDatum";
        return getCosts(projectCode, fromDate, toDate, sql);
    }

    public LinkedHashMap<Period, BigDecimal> getRDTimeCosts(String projectCode, java.sql.Date fromDate, java.sql.Date toDate){
        String sql = "select Periode as month, Jahr as year, sum(MengeErf) from sap where Projektdef=? and BuchDatum between ? and ? and Objektbezeichnung='Research & Development Trnava' and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' and KostenartenBez='Entwicklungskosten' group by Periode, Jahr order by BuchDatum";
        return getCosts(projectCode, fromDate, toDate, sql);
    }

    public LinkedHashMap<Period, BigDecimal> getCosts(String projectCode, java.sql.Date fromDate, java.sql.Date toDate, String sql) {
        LinkedHashMap<Period, BigDecimal> costs = new LinkedHashMap<>();

        try (PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, projectCode);
            preparedStatement.setDate(2, fromDate);
            preparedStatement.setDate(3, toDate);


            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
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

    public LinkedHashMap<Period, BigDecimal> getPrototypeRevenues(String projectCode, java.sql.Date fromDate, java.sql.Date toDate) {
        LinkedHashMap<Period, BigDecimal> costs = new LinkedHashMap<>();
        String sql = "select Periode as month, Jahr as year, sum(WertKWahr) from sap where Projektdef=? and BuchDatum between ? and ? and Objektbezeichnung='Samples + Revenues Trnava' and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' and WertKWahr<=0 group by Periode, Jahr order by BuchDatum";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){
            preparedStatement.setString(1,projectCode);
            preparedStatement.setDate(2,fromDate);
            preparedStatement.setDate(3, toDate);


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

    public LinkedHashMap<String, BigDecimal> getRevenuesPerForm(String projektDef, java.sql.Date fromDate, java.sql.Date toDate){
        LinkedHashMap<String, BigDecimal> revenues = new LinkedHashMap<>();

        String sql = "select Objektbezeichnung, sum(WertKWahr) from sap where Projektdef=? and" +
                " (Objektbezeichnung='R&D revenues Trnava' or Objektbezeichnung='Samples + Revenues Trnava' or Objektbezeichnung='Molds and Tool costs + Revenues Trnava')" +
                "and BuchDatum between ? and ? and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%'" +
                " and WertKWahr<=0 group by Objektbezeichnung";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){

            preparedStatement.setString(1, projektDef);
            preparedStatement.setDate(2,fromDate);
            preparedStatement.setDate(3, toDate);


            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                BigDecimal amount = rs.getBigDecimal(2);
                amount = amount.multiply(BigDecimal.valueOf(-1));
                revenues.put(rs.getString(1),amount);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenues;
    }

    public LinkedHashMap<String, BigDecimal> getCostsPerForm(String projektDef, java.sql.Date fromDate, java.sql.Date toDate){
        LinkedHashMap<String, BigDecimal> costs = new LinkedHashMap<>();

        String sql = "select Objektbezeichnung, sum(WertKWahr) from sap where Projektdef=? and " +
                "(Objektbezeichnung='Research & Development Trnava' or ((Objektbezeichnung='Samples + Revenues Trnava' or Objektbezeichnung='Molds and Tool costs + Revenues Trnava') and WertKWahr>0)) " +
                "and BuchDatum between ? and ? and  Partnerobjekt  NOT LIKE '%Ergebnisrechnung%'" +
                " and WertKWahr>0 group by Objektbezeichnung";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){

            preparedStatement.setString(1, projektDef);
            preparedStatement.setDate(2,fromDate);
            preparedStatement.setDate(3, toDate);


            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                costs.put(rs.getString(1),rs.getBigDecimal(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return costs;
    }
}
