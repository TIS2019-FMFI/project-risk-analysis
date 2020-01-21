package app.service;

import app.config.DbContext;
import app.db.SAP;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SAPService {

    private static SAPService sapService = new SAPService();

    public static SAPService getSapService() {
        return sapService;
    }

    public List<SAP> getSapDataInInterval(String projectDef, java.sql.Date from, java.sql.Date to) throws ParseException {
        List<SAP> data = new ArrayList<>();
        String sql = "select * from sap where Projektdef=? and BuchDatum between ? and ?";
        try (PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)) {

            preparedStatement.setString(1, projectDef);
            preparedStatement.setDate(2, from);
            preparedStatement.setDate(3, to);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(preparedStatement.toString());

            System.out.println(preparedStatement.toString());
            while (rs.next()) {

                SAP sap = new SAP();
                sap.setProjektDef(rs.getString(1));
                sap.setPSPElement(rs.getString(2));
                sap.setObjektbezeichnung(rs.getString(3));
                sap.setKostenart(rs.getString(4));
                sap.setKostenartenBez(rs.getString(5));
                sap.setBezeichnung(rs.getString(6));
                sap.setPartnerobjekt(rs.getString(7));
                sap.setPeriode(rs.getString(8));
                sap.setJahr(rs.getString(9));
                sap.setBelegnr(rs.getString(10));
                sap.setBuchDatum(rs.getDate(11));
                sap.setWertKWahr(rs.getBigDecimal(12));
                sap.setKWahr(rs.getString(13));
                sap.setMengeErf(rs.getDouble(14));
                sap.setGME(rs.getString(15));
                data.add(sap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public Date getFirstRecordsDate(String projectDef) {

        Date date = null;
        String sql = "select BuchDatum from sap where Projektdef=? order by BuchDatum limit 1";
        try (PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, projectDef);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                date = rs.getDate(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return date;
    }
}