package app.transactions;

import app.config.DbContext;
import app.config.SignedUser;
import app.db.User;
import app.service.UserService;

import javax.security.auth.login.LoginException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginTransaction {

    /**
     * Transakcia prihlasenia
     * @param email email uzivatela
     * @param password heslo uzivatela
     * @throws SQLException chyba pri ziskavani dat z databazy
     * @throws LoginException vlastna vynimka, udaje zadane pri prihlaseni nie su validne
     */
    public static void login(String email, String password) throws SQLException, LoginException {
        try {
            //najde uzivatela v databaze
            User user = UserService.getInstance().findUserByEmail(email);

            //skontroluj, ci je uzivatel v systeme
            isInSystem(user);

            //skontroluj heslo
            checkPassword(user, password);

            SignedUser.setUser(user);
        } catch (SQLException e) {
            throw e;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overenie hesla
     * @param user pouzivatel, ktory sa prihlasuje
     * @param password zadane heslo
     * @throws NoSuchAlgorithmException chyba pri hashovani do MD5
     * @throws UnsupportedEncodingException chyba pri hashovani do MD5
     * @throws LoginException vlastna vynimka pri prihlasovani, udaje nie su validne
     */
    private static void checkPassword(User user, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, LoginException {
        String md = org.apache.commons.codec.digest.DigestUtils.md5Hex(password);
        boolean isCorrect = user.getPassword().equals(md);

        if(!isCorrect) {
            throw new LoginException("Nesprávne heslo");
        }
        else if(password.isEmpty()) {
            throw new LoginException("Zadaj heslo");
        }
    }

    /**
     * Overenie, ci je pouziatel v systeme
     * @param user
     * @throws LoginException
     */
    private static void isInSystem(User user) throws LoginException {
        if (user == null) {
            throw new LoginException("Používateľ neexistuje");
        }
    }
}
