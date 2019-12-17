package app.service;


import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class Service<T> {

    public abstract T Objekt(ResultSet r) throws SQLException;


    public T findById(Integer id, String sql) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {

                if (r.next()) {
                    T v = Objekt(r);
                    if (r.next()) {
                        throw new RuntimeException("Too many lines");
                    }
                    return v;
                } else {
                    return null;
                }
            }
        }
    }


    public T findByEmail(String email, String sql) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            s.setString(1, email);
            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    T v = Objekt(r);
                    if (r.next()) {
                        throw new RuntimeException("Too many lines");
                    }
                    return v;
                } else {
                    return null;
                }
            }
        }
    }

}
