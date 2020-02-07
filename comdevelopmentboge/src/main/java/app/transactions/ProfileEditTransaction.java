package app.transactions;

import app.config.DbContext;
import app.config.SignedUser;
import app.exception.ProfileChangeException;
import app.gui.MyAlert;

import javax.security.auth.login.LoginException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

public class ProfileEditTransaction {

    /**
     * Transakcia zmeny emailu užívateľa
     * @param email
     * @throws SQLException
     * @throws ProfileChangeException
     */
    public static void changeEmail(String email) throws SQLException, ProfileChangeException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        String previousEmail = SignedUser.getUser().getEmail();
        try {
            //skontroluje spravny format emailu
            validateEmail(email);

            SignedUser.getUser().setEmail(email);
            SignedUser.getUser().update();

            //commitne zmeny do databazy
            DbContext.getConnection().commit();
            System.out.println("commit");


        } catch (SQLException | ProfileChangeException e) {
            SignedUser.getUser().setEmail(previousEmail);
            MyAlert.showError( "Email sa nepodarilo zmeniť");
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    /**
     * Transakcia zmeny hesla užívateľa
     * @param password
     * @throws SQLException
     * @throws ProfileChangeException
     */
    public static void changePassword(String password) throws SQLException, ProfileChangeException {
        DbContext.getConnection().setAutoCommit(false);
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        String previousPassword = SignedUser.getUser().getPassword();
        try {
            //skontroluje spravny format emailu
            validatePassword(password);

            SignedUser.getUser().setPassword(MD5Password(password));

            SignedUser.getUser().update();
            //commitne zmeny do databazy
            DbContext.getConnection().commit();

        } catch (SQLException | ProfileChangeException e) {
            SignedUser.getUser().setPassword(previousPassword);
            MyAlert.showError( "Heslo sa nepodarilo zmeniť");
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    /**
     * Validácia emailu
     * @param email
     * @throws ProfileChangeException
     */
    private static void validateEmail(String email) throws ProfileChangeException {
        if (!isEmailFormatValid(email)) {
            throw new ProfileChangeException("Nesprávny formát e-mailovej adresy");
        }
    }

    /**
     * Validácia hesla
     * @param password
     * @throws ProfileChangeException
     */
    private static void validatePassword(String password) throws ProfileChangeException {
        if (!isPasswordFormatValid(password)) {
            throw new ProfileChangeException("Heslo musí obsahovať aspoň jedno číslo, jedno písmeno a musí mať dĺžku aspoň 6 znakov");
        }
    }

    /**
     * Validácia formátu hesla
     * @param password
     * @return
     */
    private static boolean isPasswordFormatValid(String password) {
        return password.matches("(?=.*?[0-9])(?=.*?[A-Za-z]).+") && password.length() >= 6;
    }

    /**
     * Validácia formátu emailu
     * @param email
     * @return
     */
    private static boolean isEmailFormatValid(String email) {
        return email.matches("[a-zA-Z0-9\\._]+@[a-zA-Z0-9\\._]+\\.[a-zA-Z]{2,5}");
    }

    /**
     * Heslo v MD5 kódovaní
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws LoginException
     */
    private static String MD5Password(String password) {
        String md = org.apache.commons.codec.digest.DigestUtils.md5Hex(password);
        return md;
    }
}
