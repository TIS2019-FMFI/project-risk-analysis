package app.db;

import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer extends Crud<Customer>{
    private Integer id;
    private String name;

    public Customer(){};

    public Customer(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    public Integer insert() throws SQLException {
        String sql = "insert into customers (name) values (?)";
        return super.insert(DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS), 1);
    }

    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        s.setString(1, name);
        return s;
    }
}
