package app.service;

import app.db.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService extends Service<User> {

    private static final UserService INSTANCE = new UserService();

    public static UserService getInstance() { return INSTANCE; }

    private UserService() {}

    @Override
    public User Objekt(ResultSet r) throws SQLException {
        User c = new User();
        c.setId(r.getInt("id"));
        c.setName(r.getString("name"));
        c.setSurname(r.getString("surname"));
        c.setEmail(r.getString("email"));
        c.setPassword(r.getString("password"));
        c.setUserType(User.USERTYPE.valueOf(r.getString("userType")));
        c.setDeleted(r.getBoolean("deleted"));
        c.setApproved(r.getBoolean("approved"));
        return c;
    }


    public User findUserByEmail(String email) throws SQLException {
        return super.findByEmail(email,"SELECT * FROM users WHERE BINARY email = ?");
    }

    public User findUserById(Integer id) throws SQLException{
        return super.findById(id,"SELECT * FROM users WHERE id = ?");
    }


}
