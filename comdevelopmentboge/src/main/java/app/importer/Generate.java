package app.importer;

import app.config.DbContext;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static app.importer.ExcellParser.readFromFile;

public class Generate {
    public static void createAndGenerate() throws IOException {


        String sqlCreate;
        String sqlGenerate;


//SAP

        sqlCreate = "CREATE TABLE SAP (" +
                "Projektdef varchar(50)," +
                "PSPElement varchar(50)," +
                "Objektbezeichnung varchar(50)," +
                "Kostenart varchar(50)," +
                "KostenartenBez varchar(50)," +
                "Bezeichnung varchar(50)," +
                "Partnerobject varchar(50)," +
                "Periode varchar(50)," +
                "Jahr varchar(50)," +
                "Belegnr varchar(50)," +
                "BuchDatum varchar(50)," +
                "WertKWahr varchar(50)," +
                "KWahr varchar(50)," +
                "MengeErf varchar(50)," +
                "GME varchar(50))";


        sqlGenerate = "INSERT INTO SAP " +
                "(Projektdef,PSPElement,Objektbezeichnung,Kostenart,KostenartenBez,Bezeichnung,Partnerobject,Periode,Jahr,Belegnr,BuchDatum,WertKWahr,MengeErf,GME)"+
                "VALUES"+
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS SAP");
            s.executeUpdate(sqlCreate);
            System.out.println("Table SAP created");
        } catch (SQLException e) {
            System.out.println("Table SAP failed");
            e.printStackTrace();
        }

        List temp = (List) readFromFile();
        ArrayList list = new ArrayList(temp.subList(1,50));
        System.out.println(list);

        Set<List> projects = new HashSet<>();


        try(PreparedStatement s = DbContext.getConnection().prepareStatement(sqlGenerate)){
            for(Object tmp : list){
                ArrayList arr = (ArrayList) tmp;

                for(int i = 0; i < arr.size();i++){
                    s.setString(i+1,arr.get(i).toString());
                }
                projects.add(Arrays.asList(arr.get(0).toString(),"","","","","","","",""));

                s.executeUpdate();


            }

        } catch (SQLException e) {

            e.printStackTrace();
        }


//PROJECTS

        sqlCreate = "CREATE TABLE PROJECTS (" +
                "projectNumber varchar(50)," +
                "projectName varchar(50)," +
                "partNumber varchar(50)," +
                "ROS varchar(50)," +
                "ROCE varchar(50)," +
                "volumes varchar(50)," +
                "DDCost varchar(50)," +
                "prototypeCosts varchar(50)," +
                "lastUpdate varchar(50))";

        sqlGenerate = "INSERT INTO PROJECTS"+
                "(projectNumber,projectName,partNumber,ROS,ROCE,volumes,DDCost,prototypeCosts,lastUpdate)"+
                "VALUES"+
                "(?,?,?,?,?,?,?,?,?)";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS PROJECTS");
            s.executeUpdate(sqlCreate);
            System.out.println("Table PROJECTS created");
        } catch (SQLException e) {
            System.out.println("Table PROJECTS failed");
            e.printStackTrace();
        }

        try(PreparedStatement s = DbContext.getConnection().prepareStatement(sqlGenerate)) {
            for(List arr : projects){

                s.setString(1,arr.get(0).toString());
                s.setString(2,arr.get(1).toString());
                s.setString(3,arr.get(2).toString());
                s.setString(4,arr.get(3).toString());
                s.setString(5,arr.get(4).toString());
                s.setString(6,arr.get(5).toString());
                s.setString(7,arr.get(6).toString());
                s.setString(8,arr.get(7).toString());
                s.setString(9,arr.get(8).toString());
                s.executeUpdate();

            }
            System.out.println("Table PROJECTS filled");
        } catch (SQLException e) {
            System.out.println("PROJECTS INSERT FAILED");
            e.printStackTrace();
        }




//USERS

        ArrayList<List> users = new ArrayList();
        users.add(Arrays.asList("Adam","Mrkvicka","adam@boge.com","nenavidimMrkvu","admin",true,false));
        users.add(Arrays.asList("Peter","Zahradka","peter@boge.com","pestujemTravicku","user",true,false));
        users.add(Arrays.asList("Jozef","Strom","jozef@boge.com","stromySuLaska","projectAdmin",true,false));
        users.add(Arrays.asList("Anna","Bobrova","adam@boge.com","boborIbaVMene","fem",true,false));
        users.add(Arrays.asList("Lojzo","Hipster","lojzo@boge.com","rawVeganForLife","user",false,false));

        sqlCreate = "CREATE TABLE USERS (" +
                "name varchar(50)," +
                "surname varchar(50)," +
                "email varchar(50)," +
                "password varchar(50)," +
                "userType varchar(50)," +
                "approved boolean," +
                "deleted boolean)";

        sqlGenerate = "INSERT INTO USERS"+
                "(name,surname,email,password,userType,approved,deleted)"+
                "VALUES"+
                "(?,?,?,?,?,?,?)";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS USERS");
            s.executeUpdate(sqlCreate);
            System.out.println("Table USERS created");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(PreparedStatement s = DbContext.getConnection().prepareStatement(sqlGenerate)) {
            for(List arr : users){
                s.setString(1,arr.get(0).toString());
                s.setString(2,arr.get(1).toString());
                s.setString(3,arr.get(2).toString());
                s.setString(4,arr.get(3).toString());
                s.setString(5,arr.get(4).toString());
                s.setBoolean(6,(Boolean)arr.get(5));
                s.setBoolean(7,(Boolean)arr.get(6));
                s.executeUpdate();
            }
            System.out.println("Table USERS filled");
        } catch (SQLException e) {
            System.out.println("USER INSERT FAILED");
            e.printStackTrace();
        }


//REMINDERS

        sqlCreate = "CREATE TABLE REMINDERS (" +
                "text varchar(50)," +
                "partNumber varchar(50)," +
                "timePeriod varchar(50)," +
                "type varchar(50))";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS REMINDERS");
            s.executeUpdate(sqlCreate);
            System.out.println("Table REMINDERS created");
        } catch (SQLException e) {
            e.printStackTrace();
        }


//LOGS

        sqlCreate = "CREATE TABLE LOGS (" +
                "text varchar(50)," +
                "date varchar(50)," +
                "time varchar(50))";


        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS LOGS");
            s.executeUpdate(sqlCreate);
            System.out.println("Table LOGS created");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
