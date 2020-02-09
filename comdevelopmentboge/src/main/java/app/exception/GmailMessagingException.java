package app.exception;

public class GmailMessagingException extends MyException {
    public static final String ERROR = "Niečo sa pokazilo pri odosielaní mailov. \n Kontaktujte správcu.";
    public GmailMessagingException() {
        super(ERROR);
    }
}
