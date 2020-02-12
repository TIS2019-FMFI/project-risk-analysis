package app.exception;

public class DatabaseException extends MyException {
    /**
     * Text vynimky, ktory sa zobrazi
     */
    public static final String ERROR = "Niečo sa pokazilo pri komunikácii s databázou. \n Kontaktuje správcu. ";

    /**
     * Vlastna vynimka, pri chybe ziskavania dat z databazy
     * @param mes text vynimky
     */
    public DatabaseException(String mes) {super(mes);}

    /**
     * Nastavenie textu vynimky
     */
    public DatabaseException() {
            super(ERROR);
    }
}
