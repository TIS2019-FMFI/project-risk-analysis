package app.service;

import app.config.DbContext;
import app.db.ProjectReminder;
import app.db.RegistrationRequest;
import app.transactions.Registration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationRequestService extends Service<RegistrationRequest> {
    private static RegistrationRequestService instance = new RegistrationRequestService();
    public static RegistrationRequestService getInstance(){ return  instance;}

    @Override
    public RegistrationRequest Objekt(ResultSet r) throws SQLException {
        RegistrationRequest req = new RegistrationRequest();
        req.setUser_id(r.getInt("user_id"));
        req.setText(r.getString("text"));
        return req;
    }


    public List<RegistrationRequest> findAll() throws SQLException {
        return findAll("select * from registration_requests");
    }
}
