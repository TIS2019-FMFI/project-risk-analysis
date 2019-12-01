package app.transactions;

import app.config.DbContext;
import app.db.RegistrationRequest;
import app.db.Users;
import app.exception.RegistrationException;

import java.sql.Connection;
import java.sql.SQLException;

public class Registration {
    public static void register(String name,String surname,String email,String password) throws SQLException, RegistrationException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            //zvaliduje formaty vstupnych udajov
            //vyhodi chybu ak je aspon jeden nespravny
            validateInputsFormat(name,surname,email,password);

            //vytvori noveho uzivatela
            Users user = createUser(name, surname, email, password);

            //vlozi uzivatela do databazy a vrati jeho ID
            Integer ID = user.insert();
            user.setId(ID);

            //vytvori ziadost o registraciu a vlozi ju do tabulky
            RegistrationRequest req = createRegistrationRequest(user);
            req.insert();

            //commitne zmeny do databazy
            DbContext.getConnection().commit();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }
    private static RegistrationRequest createRegistrationRequest(Users user) {
        RegistrationRequest req = new RegistrationRequest();
        req.setText("Používateľ"+ user.getFullName() +"žiada o registráciu");
        req.setUser_id(user.getId());
        return req;
    }
    private static Users createUser(String name, String surname, String email, String password) {
        Users user = new Users();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserType(Users.USERTYPE.USER);
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
        return email.matches("[a-zA-Z0-9._]+@[a-zA-Z0-9._]\\.[a-zA-Z]{2,5}");
    }
    private static boolean containsOnlyLetters(String value) {
        return value.matches("^[A-Za-z]+$");
    }
}
