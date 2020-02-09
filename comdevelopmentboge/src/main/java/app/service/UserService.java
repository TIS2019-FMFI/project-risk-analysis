package app.service;

import app.config.SignedUser;
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
        return super.findByEmail(email,"SELECT * FROM users WHERE BINARY email = ? and deleted = false");
    }

    public User findUserById(Integer id) throws SQLException{
        return super.findById(id,"SELECT * FROM users WHERE id = ? and deleted = false");
    }

    public User findUserByFullName(String fullName) throws SQLException {
        String[] nameSurname = fullName.split(" ");
        String name = nameSurname[0];
        String surname = nameSurname[1];
        return super.findByAttributes(name, surname, "SELECT * FROM users WHERE name = ? AND surname = ? and deleted = false");
    }

    public List<User> findAllUsers() throws SQLException {
        Integer id = SignedUser.getUser().getId();
        return super.findAllExcept("SELECT * FROM users where id != ? and deleted = false", id);
    }


}