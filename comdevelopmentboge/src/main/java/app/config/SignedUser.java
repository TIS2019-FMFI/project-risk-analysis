package app.config;

import app.db.User;

public class SignedUser {
    /**
     * Prihlaseny uzivatel
     */
    private static User user;

    /**
     * Vrati instanciu uzivatela
     * @return instancia uzivatela
     */
    public static User getUser() {
        if (user == null) {
            throw new IllegalStateException("user must be set before calling this method");
        }
        return user;
    }

    /**
     * Nastavi prihlaseneho uzivatela
     * @param user0 prihlaseny uzivatel
     */
    public static void setUser(final User user0) {
        user = user0;
    }



    private SignedUser() {}
}
