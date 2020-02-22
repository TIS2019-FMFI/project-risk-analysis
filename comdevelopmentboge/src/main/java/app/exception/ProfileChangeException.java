package app.exception;

public class ProfileChangeException extends MyException {
    /**
     * Vlastna vynimka pri administracii profilu
     * @param message - text vynimky
     */
    public ProfileChangeException(String message) {
        super(message);
    }
}