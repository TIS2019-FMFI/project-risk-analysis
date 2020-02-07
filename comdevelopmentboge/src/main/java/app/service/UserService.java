package app.service;

import app.db.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    public User findUserByFullName(String fullName) throws SQLException {
        String[] nameSurname = fullName.split(" ");
        String name = nameSurname[0];
        String surname = nameSurname[1];
        return super.findByAttributes(name, surname, "SELECT * FROM users WHERE name = ? AND surname = ?");
    }

    public List<User> findAllUsers() throws SQLException {
        return super.findAll("SELECT * FROM users");
    }


}
