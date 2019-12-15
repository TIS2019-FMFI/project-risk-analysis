package app.service;

import app.config.DbContext;
import app.db.SAP;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SAPService {

    private static SAPService sapService = new SAPService();
    public static SAPService getSapService(){return sapService;}

    public List<SAP> getAllSAPData() throws ParseException {
        List<SAP> data = new ArrayList<>();
        String sql = "select * from sap";
        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){

                SAP sap = new SAP();
                sap.setPSPElement(rs.getString(1));
                sap.setObjektbezeichnung(rs.getString(2));
                sap.setKostenart(rs.getString(3));
                sap.setKostenartenBez(rs.getString(4));
                sap.setPartnerojekt(rs.getString(5));
                sap.setPeriode(rs.getString(6));
                sap.setJahr(rs.getString(7));

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                sap.setBuchDatum((Date) sdf.parse(rs.getString(8)));
                sap.setWert(rs.getBigDecimal(9));
                sap.setMenge(rs.getDouble(10));
                sap.setGME(rs.getString(11));

                data.add(sap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}
