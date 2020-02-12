package app.exception;

public class MyException extends Exception {
    /**
     * Vlastna vynimka
     * @param message text vynimky
     */
    public MyException(String message) {
        super(message);
    }
}
