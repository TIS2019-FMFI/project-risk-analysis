package app.exception;

public class ConfigurationException extends MyException {
    /**
     * Vlastna vynimka, pri chybe konfiguracie
     * @param message text vynimky
     */
    public ConfigurationException(String message) {super(message);}
}
