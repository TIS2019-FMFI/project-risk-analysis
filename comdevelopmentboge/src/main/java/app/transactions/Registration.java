package app.transactions;

import app.config.DbContext;
import app.config.SignedUser;
import app.db.RegistrationRequest;
import app.db.User;
import app.exception.DatabaseException;
import app.exception.RegistrationException;
import app.service.UserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class Registration {
    public static void declineRegistrationRequest(RegistrationRequest request) throws SQLException, DatabaseException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            //ziadost sa vymaze
            request.delete();

            //ziska sa uzivatel a zmeni sa mu deleted na true
            User user = UserService.getInstance().findUserById(request.getUser_id());
            user.setDeleted(true);

            //zmeny sa poslu do databazy
            user.update();

        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }
    public static void approveRegistrationRequest(RegistrationRequest request) throws SQLException, DatabaseException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            //ziadost sa vymaze
            request.delete();

            //ziska sa uzivatel a zmeni sa mu approved na true
            User user = UserService.getInstance().findUserById(request.getUser_id());
            user.setApproved(true);

            //zmeny sa poslu do databazy
            user.update();

        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }
    public static boolean isRegistrationApproved(User user0) throws SQLException {
        User user = UserService.getInstance().findUserById(user0.getId());
        return user.getApproved();
    }
    public static void register(String name,String surname,String email,String password) throws SQLException, RegistrationException, DatabaseException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {

            //skontroluje ci nahodou nie je uzivatel uz registrovany
            User user = UserService.getInstance().findUserByEmail(email);
            if (user != null) {
                throw new RegistrationException("Už ste registrovaný");
            }

            //zvaliduje formaty vstupnych udajov
            //vyhodi chybu ak je aspon jeden nespravny
            validateInputsFormat(name,surname,email,password);

            //vytvori noveho uzivatela
            user = createUser(name, surname, email, password);

            //vlozi uzivatela do databazy a nastavi jeho ID
            user.insert();

            //vytvori ziadost o registraciu a vlozi ju do tabulky
            RegistrationRequest req = createRegistrationRequest(user);
            req.insert();

            //commitne zmeny do databazy
            DbContext.getConnection().commit();

            //ked sa podari comit tak ho rovno nastavi ako signed user

            SignedUser.setUser(user);
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }
    private static RegistrationRequest createRegistrationRequest(User user) {
        RegistrationRequest req = new RegistrationRequest();
        req.setText("Používateľ "+ user.getFullName() +" žiada o registráciu");
        req.setUser_id(user.getId());
        return req;
    }
    private static User createUser(String name, String surname, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(org.apache.commons.codec.digest.DigestUtils.md5Hex(password));
        user.setUserType(User.USERTYPE.USER);
        user.setApproved(false);
        user.setDeleted(false);
        return user;
    }
    private static void validateInputsFormat(String name,String surname,String email,String password) throws RegistrationException {
        if(!containsOnlyLetters(name)) {
            throw new RegistrationException("Meno môže obsahovať iba písmená");
        }
        if(!containsOnlyLetters(surname)) {
            throw new RegistrationException("Priezvisko môže obsahovať iba písmená");
        }
        if (!isEmailFormatValid(email)) {
            throw new RegistrationException("Nesprávny formát e-mailovej adresy");
        }
        if (!isPasswordFormatValid(password)) {
            throw new RegistrationException("Heslo musí obsahovať aspoň jedno číslo, jedno písmeno a musí mať dĺžku aspoň 6 znakov");
        }

    }
    private static boolean isPasswordFormatValid(String password) {
        return password.matches("(?=.*?[0-9])(?=.*?[A-Za-z]).+") && password.length() >= 6;
    }
    private static boolean isEmailFormatValid(String email) {
        return email.matches("[a-zA-Z0-9\\._]+@[a-zA-Z0-9\\._]+\\.[a-zA-Z]{2,5}");
    }
    private static boolean containsOnlyLetters(String value) {
        return value.matches("^[A-Za-z]+$");
    }
}
