package app.config;

import app.db.User;

public class SignedUser {
    private static User user;

    public static User getUser() {
        if (user == null) {
            throw new IllegalStateException("user must be set before calling this method");
        }
        return user;
    }

    public static void setUser(final User user0) {
        user = user0;
    }



    private SignedUser() {}
}
