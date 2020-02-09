package app.service;

import app.config.DbContext;
import app.db.ProjectCosts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectCostsService {
    private static ProjectCostsService instance = new ProjectCostsService();
    public static ProjectCostsService getInstance(){ return  instance;}

    public List<ProjectCosts> getDDCosts() throws SQLException {
        List<ProjectCosts> result = new ArrayList<>();

        String sql = "select Projektdef as projectNumber, p.id, sum(WertKWahr) as actual, p.DDCost as planned " +
                "from sap s left join projects p " +
                "on s.projektdef = p.projectNumber " +
                "where  " +
                "Objektbezeichnung='Research & Development Trnava' and " +
                "Partnerobjekt  NOT LIKE '%Ergebnisrechnung%' and " +
                "KostenartenBez='Entwicklungskosten' " +
                "group by Projektdef " +
                "having actual >= p.DDCost*0.9 and p.DDCost > 0";

        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                result.add(load(rs,ProjectCosts.projectCostsType.DD));
            }

        }

        return result;
    }

    public List<ProjectCosts> getPrototypeCosts() throws SQLException {
        List<ProjectCosts> result = new ArrayList<>();

        String sql = "select Projektdef as projectNumber, p.id, sum(WertKWahr) as actual, p.prototypeCosts as planned \n" +
                "from sap s left join projects p \n" +
                "on s.projektdef = p.projectNumber \n" +
                "where  \n" +
                "(Objektbezeichnung='Samples + Revenues Trnava' or Objektbezeichnung='Molds and Tool costs + Revenues Trnava') \n" +
                "and Partnerobjekt  NOT LIKE '%Ergebnisrechnung%'  \n" +
                "and WertKWahr>0  \n" +
                "group by Projektdef \n" +
                "having actual >= p.prototypeCosts*0.9 and p.prototypeCosts > 0 ";

        try(PreparedStatement preparedStatement = DbContext.getConnection().prepareStatement(sql)){

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                result.add(load(rs,ProjectCosts.projectCostsType.PROTOTYPE));
            }

        }

        return result;
    }
    private ProjectCosts load(ResultSet rs, ProjectCosts.projectCostsType type) throws SQLException {
        ProjectCosts p = new ProjectCosts();
        p.setType(type);
        p.setProjectNumber(rs.getString("projectNumber"));
        p.setProject_id(rs.getInt("id"));
        p.setActualCosts(rs.getDouble("actual"));
        p.setPlannedCosts(rs.getDouble("planned"));

        return p;
    }
}
