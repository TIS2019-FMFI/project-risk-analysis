package app.exception;

public class DatabaseException extends MyException {
    public static final String ERROR = "Niečo sa pokazilo pri komunikácii s databázou. \n Kontaktuje správcu. ";
    public DatabaseException(String mes) {super(mes);}
    public DatabaseException() {
            super(ERROR);
    }
}
