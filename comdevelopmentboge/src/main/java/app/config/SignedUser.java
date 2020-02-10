package app.config;

import app.db.User;

public class SignedUser {
    /**
     * Prihlásený užívateľ
     */
    private static User user;

    /**
     * Vráti inštanciu užívateľa
     * @return
     */
    public static User getUser() {
        if (user == null) {
            throw new IllegalStateException("user must be set before calling this method");
        }
        return user;
    }

    /**
     * Nastaví prihláseného užívateľa
     * @param user0
     */
    public static void setUser(final User user0) {
        user = user0;
    }



    private SignedUser() {}
}
