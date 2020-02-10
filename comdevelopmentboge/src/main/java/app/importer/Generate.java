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

        sqlCreate = "CREATE TABLE sap (" +
                "Projektdef varchar(50)," +
                "PSPElement varchar(50)," +
                "Objektbezeichnung varchar(50)," +
                "Kostenart varchar(50)," +
                "KostenartenBez varchar(50)," +
                "Bezeichnung varchar(50)," +
                "Partnerobjekt varchar(50)," +
                "Periode varchar(50)," +
                "Jahr varchar(50)," +
                "Belegnr varchar(50)," +
                "BuchDatum date," +
                "WertKWahr double," +
                "KWahr varchar(50)," +
                "MengeErf varchar(50)," +
                "GME varchar(50))";


        sqlGenerate = "INSERT INTO sap " +
                "(Projektdef,PSPElement,Objektbezeichnung,Kostenart,KostenartenBez,Bezeichnung,Partnerobjekt,Periode,Jahr,Belegnr,BuchDatum,WertKWahr,KWahr,MengeErf,GME)"+
                "VALUES"+
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS sap");
            s.executeUpdate(sqlCreate);
            System.out.println("Table sap created");
        } catch (SQLException e) {
            System.out.println("Table sap failed");
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
//CUSTOMERS

        sqlCreate = "CREATE TABLE customers ( id int primary key AUTO_INCREMENT, " +
                "name varchar(50) UNIQUE NOT NULL)";

        sqlGenerate = "insert into customers (name) values ('audi')";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS customers");
            s.executeUpdate(sqlCreate);
            System.out.println("Table customers created");
        } catch (SQLException e) {
            e.printStackTrace();
        }




//PROJECTS

        sqlCreate = "CREATE TABLE projects (" +
                "id int primary key auto_increment,"+
                "projectNumber varchar(50) UNIQUE NOT NULL," +
                "projectName varchar(50) NOT NULL," +
                "partNumber varchar(50)," +
                "ROS varchar(50)," +
                "ROCE varchar(50)," +
                "volumes varchar(50)," +
                "DDCost double," +
                "prototypeCosts double," +
                "lastUpdate date," +
                "customer_id int references customers(id))";

        sqlGenerate = "INSERT INTO projects"+
                "(projectNumber,projectName,partNumber,ROS,ROCE,volumes,DDCost,prototypeCosts,lastUpdate, customer_id)"+
                "VALUES"+
                "(?,?,?,?,?,?,?,?,?,?)";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS projects");
            s.executeUpdate(sqlCreate);
            System.out.println("Table projects created");
        } catch (SQLException e) {
            System.out.println("Table projects failed");
            e.printStackTrace();
        }

        int projectNr = 0;
        try(PreparedStatement s = DbContext.getConnection().prepareStatement(sqlGenerate)) {
            for(List arr : projects){

                s.setString(1,arr.get(0).toString());
                s.setString(2,"project"+projectNr++);
                s.setString(3,arr.get(2).toString());
                s.setString(4,arr.get(3).toString());
                s.setString(5,arr.get(4).toString());
                s.setString(6,arr.get(5).toString());
                s.setBigDecimal(7, BigDecimal.ZERO);
                s.setBigDecimal(8,BigDecimal.ZERO);
                s.setDate(9,null);
                s.setInt(10, 1);
                s.executeUpdate();

            }
            System.out.println("Table projects filled");
        } catch (SQLException e) {
            System.out.println("projects INSERT FAILED");
            e.printStackTrace();
        }




//USERS

        ArrayList<List> users = new ArrayList();
        users.add(Arrays.asList("Adam","Mrkvicka","adam@boge.com","nenavidimMrkvu","CENTRAL_ADMIN",true,false));
        users.add(Arrays.asList("Peter","Zahradka","peter@boge.com","pestujemTravicku","USER",true,false));
        users.add(Arrays.asList("Jozef","Strom","jozef@boge.com","stromySuLaska","PROJECT_ADMIN",true,false));
        users.add(Arrays.asList("Anna","Bobrova","anna@boge.com","boborIbaVMene","USER",true,false));
        users.add(Arrays.asList("Lojzo","Hipster","lojzo@boge.com","rawVeganForLife","USER",false,false));

        sqlCreate = "CREATE TABLE users (" +
                "id integer primary key AUTO_INCREMENT,"+
                "name varchar(50) NOT NULL," +
                "surname varchar(50) NOT NULL," +
                "email varchar(50)," +
                "password varchar(50)," +
                "userType varchar(50)," +
                "approved boolean," +
                "deleted boolean NOT NULL)";

        sqlGenerate = "INSERT INTO users"+
                "(name,surname,email,password,userType,approved,deleted)"+
                "VALUES"+
                "(?,?,?,?,?,?,?)";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS users");
            s.executeUpdate(sqlCreate);
            System.out.println("Table users created");
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
            System.out.println("Table users filled");
        } catch (SQLException e) {
            System.out.println("user INSERT FAILED");
            e.printStackTrace();
        }

//ADMINISTRATION
        sqlCreate = "CREATE TABLE administration(" +
                "id int primary key auto_increment, " +
                "user_id int references users(id),"+
                "project_id int references projects(id)"+
                ")";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS administration");
            s.executeUpdate(sqlCreate);
            System.out.println("Table administration created");
        } catch (SQLException e) {
            e.printStackTrace();
        }

//REMINDERS

        sqlCreate = "CREATE TABLE reminders (id int primary key auto_increment, " +
                "text varchar(150) NOT NULL," +
                "project_id int references projects(id), " +
                "date date,closed boolean,unique_code varchar(150) UNIQUE,sent boolean)";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS reminders");
            s.executeUpdate(sqlCreate);
            System.out.println("Table reminders created");
        } catch (SQLException e) {
            e.printStackTrace();
        }



//REGISTRATION REQUESTS

        sqlCreate = "CREATE TABLE registration_requests ( " +
                "id int primary key auto_increment," +
                "user_id int references users(id),"+
                "text varchar(150) NOT NULL)";

        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS registration_requests");
            s.executeUpdate(sqlCreate);
            System.out.println("Table registration_requests created");
        } catch (SQLException e) {
            e.printStackTrace();
        }


//LOGS

        sqlCreate = "CREATE TABLE logs (" +
                "id int primary key AUTO_INCREMENT, "+
                "user_id int references users(id),"+
                "text longtext NOT NULL,"+
                "time timestamp NOT NULL)";



        try( Statement s = DbContext.getConnection().createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS logs");
            s.executeUpdate(sqlCreate);
            System.out.println("Table logs created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}


