package app.importer;

import app.config.DbContext;

import java.io.IOException;
import java.math.BigDecimal;
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
                "(Projektdef,PSPElement,Objektbezeichnung,Kostenart,KostenartenBez,Bezeichnung,Partnerobject,Periode,Jahr,Belegnr,BuchDatum,WertKWahr,KWahr,MengeErf,GME)"+
                "VALUES"+
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS SAP");
            s.executeUpdate(sqlCreate);
            System.out.println("Table SAP created");
        } catch (SQLException e) {
            System.out.println("Table SAP failed");
            e.printStackTrace();
        }

        ArrayList<ExcelRow> list = readFromFile();
        Set<List> projects = new HashSet<>();


        try(PreparedStatement s = DbContext.getConnection().prepareStatement(sqlGenerate)){
            for(ExcelRow tmp : list){
                ArrayList<String> data = tmp.getData();
                java.util.Date date = tmp.getDate();

                for(int i = 0; i < data.size();i++){
                    if(i == 7 || i == 8){
                        s.setInt(i+1,Integer.parseInt(data.get(i)));
                    }else if( i== 11 || i == 13){
                        s.setBigDecimal(i+1, new BigDecimal(data.get(i)));
                    }else {
                        s.setString(i + 1, data.get(i));
                    }
                }
                projects.add(Arrays.asList(data.get(0),"","","","","","","",""));
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                s.setDate(11,sqlDate);
                s.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

//PROJECTS

        sqlCreate = "CREATE TABLE PROJECTS (" +
                "projectNumber varchar(50) UNIQUE NOT NULL," +
                "projectName varchar(50) NOT NULL," +
                "partNumber varchar(50)," +
                "ROS varchar(50)," +
                "ROCE varchar(50)," +
                "volumes varchar(50)," +
                "DDCost double," +
                "prototypeCosts double," +
                "lastUpdate date)";

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
                s.setBigDecimal(7, BigDecimal.ZERO);
                s.setBigDecimal(8,BigDecimal.ZERO);
                s.setDate(9,null);
                s.executeUpdate();

            }
            System.out.println("Table PROJECTS filled");
        } catch (SQLException e) {
            System.out.println("PROJECTS INSERT FAILED");
            e.printStackTrace();
        }




//USERS

        ArrayList<List> users = new ArrayList();
        users.add(Arrays.asList("Adam","Mrkvicka","adam@boge.com","nenavidimMrkvu","ADMIN",true,false));
        users.add(Arrays.asList("Peter","Zahradka","peter@boge.com","pestujemTravicku","USER",true,false));
        users.add(Arrays.asList("Jozef","Strom","jozef@boge.com","stromySuLaska","PROJECT_ADMIN",true,false));
        users.add(Arrays.asList("Anna","Bobrova","anna@boge.com","boborIbaVMene","FEM",true,false));
        users.add(Arrays.asList("Lojzo","Hipster","lojzo@boge.com","rawVeganForLife","USER",false,false));

        sqlCreate = "CREATE TABLE USERS (" +
                "name varchar(50) NOT NULL," +
                "surname varchar(50) NOT NULL," +
                "email varchar(50)," +
                "password varchar(50)," +
                "userType varchar(50)," +
                "approved boolean," +
                "deleted boolean NOT NULL)";

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

//CUSTOMERS

        sqlCreate = "CREATE TABLE CUSTOMERS (" +
                "name varchar(50) UNIQUE NOT NULL)";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS CUSTOMERS");
            s.executeUpdate(sqlCreate);
            System.out.println("Table CUSTOMERS created");
        } catch (SQLException e) {
            e.printStackTrace();
        }


//REMINDERS

        sqlCreate = "CREATE TABLE REMINDERS (" +
                "text varchar(150) NOT NULL," +
                "partNumber varchar(50)," +
                "timePeriod varchar(50)," +
                "isFEM BOOLEAN NOT NULL)";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS REMINDERS");
            s.executeUpdate(sqlCreate);
            System.out.println("Table REMINDERS created");
        } catch (SQLException e) {
            e.printStackTrace();
        }



//REGISTRATION REQUEST

        sqlCreate = "CREATE TABLE REGISTRATION_REQUEST (" +
                "text varchar(150) NOT NULL)";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS REGISTRATION_REQUEST");
            s.executeUpdate(sqlCreate);
            System.out.println("Table REGISTRATION_REQUEST created");
        } catch (SQLException e) {
            e.printStackTrace();
        }


//LOGS

        sqlCreate = "CREATE TABLE LOGS (" +
                "text varchar(50) NOT NULL," +
                "date date NOT NULL," +
                "time time NOT NULL)";



        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS LOGS");
            s.executeUpdate(sqlCreate);
            System.out.println("Table LOGS created");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}


