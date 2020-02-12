package app.exception;

public class LoginException extends MyException {
    /**
     * Vlastna vynimka pri prihlasovani
     * @param message text vynimky
     */
    public LoginException(String message) {
        super(message);
    }
}