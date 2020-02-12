package app.exception;

public class GmailMessagingException extends MyException {
    /**
     * Text vynimky, ktory sa zobrazi
     */
    public static final String ERROR = "Niečo sa pokazilo pri odosielaní mailov. \n Kontaktujte správcu.";

    /**
     * Vlastna vynimka pri posielani emailu
     */
    public GmailMessagingException() {
        super(ERROR);
    }
}
