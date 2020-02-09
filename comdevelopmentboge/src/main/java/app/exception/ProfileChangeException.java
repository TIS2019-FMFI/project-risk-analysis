package app.exception;

public class ProfileChangeException extends MyException {
    /**
     * Výnimka pri administrácii profilu
     * @param message - text výnimky
     */
    public ProfileChangeException(String message) {
        super(message);
    }
}