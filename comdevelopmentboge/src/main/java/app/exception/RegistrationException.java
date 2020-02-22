package app.exception;

public class RegistrationException extends MyException {
    /**
     * Vlastna vynimka pri registracii
     * @param message text vynimky
     */
    public RegistrationException(String message) {
        super(message);
    }
}
