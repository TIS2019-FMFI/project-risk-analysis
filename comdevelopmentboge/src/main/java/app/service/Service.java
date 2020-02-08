package app.service;


import app.config.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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

    public T findByName(String name, String sql) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            s.setString(1, name);
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

    public T findByAttributes(String a1, String a2, String sql) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            s.setString(1, a1);
            s.setString(2, a2);
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

    public List<T> findAll(String sql) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            try (ResultSet r = s.executeQuery()) {

                List<T> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(Objekt(r));
                }

                return elements;
            }
        }
    }
    public List<T> findAll(PreparedStatement statement) throws SQLException {

            try (ResultSet r = statement.executeQuery()) {

                List<T> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(Objekt(r));
                }

                return elements;
            }

    }

    public List<T> findAllExcept(String sql, Integer id) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) {

                List<T> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(Objekt(r));
                }

                return elements;
            }
        }
    }

}
