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
     * Transakcia zmeny emailu uzivatela
     * @param email - email pouzivatela
     * @throws SQLException chyba pri ziskavani dat z databazy
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
            MyAlert.showSuccess("Email bol úspešne zmenený");

        } catch (SQLException e) {
            SignedUser.getUser().setEmail(previousEmail);
            MyAlert.showError("Email sa nepodarilo zmeniť");
            DbContext.getConnection().rollback();
        }   catch (ProfileChangeException e) {
            MyAlert.showError(e.getMessage());
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    /**
     * Transakcia zmeny hesla uzivatela
     * @param password zadane heslo
     * @throws SQLException chyba pri spusteni SQL dopytu
     * @throws ProfileChangeException vlastna vynimka, udaje nie su validne
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
            MyAlert.showSuccess("Heslo bolo úspešne zmenené");
        } catch (SQLException e) {
            SignedUser.getUser().setPassword(previousPassword);
            MyAlert.showError("Heslo sa nepodarilo zmeniť");
            DbContext.getConnection().rollback();
        } catch (ProfileChangeException e) {
            MyAlert.showError(e.getMessage());
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    /**
     * Validacia emailu
     * @param email zadany email
     * @throws ProfileChangeException vlastna vynimka, email nie je validny
     */
    private static void validateEmail(String email) throws ProfileChangeException {
        if (!isEmailFormatValid(email)) {
            throw new ProfileChangeException("Nesprávny formát e-mailovej adresy");
        }
    }

    /**
     * Validacia hesla
     * @param password zadane heslo
     * @throws ProfileChangeException vlastna vynimka, heslo nie je validne
     */
    private static void validatePassword(String password) throws ProfileChangeException {
        if (!isPasswordFormatValid(password)) {
            throw new ProfileChangeException("Heslo musí obsahovať minimálne jedno číslo, jedno písmeno\n a musí mať dĺžku minimálne 6 znakov");
        }
    }

    /**
     * Validácia formatu hesla
     * @param password zadane heslo
     * @return true alebo false
     */
    private static boolean isPasswordFormatValid(String password) {
        return password.matches("(?=.*?[0-9])(?=.*?[A-Za-z]).+") && password.length() >= 6;
    }

    /**
     * Validacia formatu emailu
     * @param email zadany email
     * @return true alebo false
     */
    private static boolean isEmailFormatValid(String email) {
        return email.matches("[a-zA-Z0-9\\._]+@boge-rubber-plastics.com");
    }

    /**
     * Heslo v MD5 kodovani
     * @param password heslo
     * @return zahashovane heslo v MD5
     */
    private static String MD5Password(String password) {
        String md = org.apache.commons.codec.digest.DigestUtils.md5Hex(password);
        return md;
    }
}
