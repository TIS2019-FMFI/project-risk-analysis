package app.service;

import app.App;
import app.config.DbContext;
import app.db.ProjectCosts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectCostsService extends Service<ProjectCosts> {
    private static ProjectCostsService instance = new ProjectCostsService();
    public static ProjectCostsService getInstance(){ return  instance;}

    public List<ProjectCosts> getDDCosts() throws SQLException {
                String sql = "select Projektdef as projectNumber, p.id, sum(WertKWahr) as actual, p.DDCost as planned " +
                "from sap s left join projects p " +
                "on s.projektdef = p.projectNumber " +
                "where  " +
                "Objektbezeichnung='Research & Development Trnava' and " +
                "Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' and " +
                "KostenartenBez='Entwicklungskosten' " +
                "group by Projektdef " +
                "having actual >= p.DDCost*? and p.DDCost > 0";
        PreparedStatement statement = DbContext.getConnection().prepareStatement(sql);
        String risk = App.getPropertiesManager().getProperty("rd.costs.risk");
        if (null == risk) {
            risk = "0.9";
        }
        statement.setDouble(1, Double.parseDouble(risk));
        List<ProjectCosts> list = findAll(statement);
        for (ProjectCosts p : list) {
            p.setType(ProjectCosts.projectCostsType.DD);
        }
        return list;
    }

    public List<ProjectCosts> getPrototypeCosts() throws SQLException {
                String sql = "select Projektdef as projectNumber, p.id, sum(WertKWahr) as actual, p.prototypeCosts as planned \n" +
                "from sap s left join projects p \n" +
                "on s.projektdef = p.projectNumber \n" +
                "where  \n" +
                "(Objektbezeichnung='Samples + Revenues Trnava' or Objektbezeichnung='Molds and Tool costs + Revenues Trnava') \n" +
                "and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%'  \n" +
                "and WertKWahr>0  \n" +
                "group by Projektdef \n" +
                "having actual >= p.prototypeCosts*? and p.prototypeCosts > 0 ";
        PreparedStatement statement = DbContext.getConnection().prepareStatement(sql);
        String risk = App.getPropertiesManager().getProperty("prototype.costs.risk");
        if (null == risk) {
            risk = "0.9";
        }
        statement.setDouble(1, Double.parseDouble(risk));

        List<ProjectCosts> list = findAll(statement);
        for (ProjectCosts p : list) {
            p.setType(ProjectCosts.projectCostsType.PROTOTYPE);
        }
        return list;
    }


    @Override
    public ProjectCosts Objekt(ResultSet r) throws SQLException {
        ProjectCosts p = new ProjectCosts();
        p.setProjectNumber(r.getString("projectNumber"));
        p.setProject_id(r.getInt("id"));
        p.setActualCosts(r.getDouble("actual"));
        p.setPlannedCosts(r.getDouble("planned"));

        return p;
    }
}
