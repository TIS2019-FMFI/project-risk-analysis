package app.config;


import app.App;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {
    /**
     * E-mailova adresa odosielatela
     */
    private static final String from = App.getPropertiesManager().getProperty("email.address");
    private static final String password = App.getPropertiesManager().getProperty("email.password");
    private static final String host = App.getPropertiesManager().getProperty("email.host");

    private static Properties props;
    private static Session session;

    private static void initialize() {
        props = new Properties();

        props.put("mail.smtp.host", host);

        session = Session.getInstance(props, null);
    }

    /**
     * Posle e-mailovu spravu s vygenerovanym heslom
     * @param recipient e-mailova adresa prijmatela (musi byt Gmail)
     * @param newPassword vygenerovane heslo
     * @throws MessagingException chyba pri odosielani emailu
     */
    public static void sendNewPassword(String recipient, String newPassword) throws MessagingException {
        sendMessage(recipient,"Nové vygenerované heslo je: " + newPassword,"Reset hesla");

    }

    /**
     * Polle notifikaciu na e-mailovu adresu
     * @param recipient e-mailova adresa prijimatela (musi byt Gmail)
     * @param text obsah e-mailu
     * @throws MessagingException chyba pri odosielani emailu
     */
    public static void sendReminder(String recipient,String text) throws MessagingException {
        sendMessage(recipient,text,"Riziko prekročenia plánovaných nákladov");
    }

    /**
     * Posle e-mailovu spravu na zaklade parametrov
     * @param recipient e-mailova adresa prijimatela (musi byt Gmail)
     * @param text obsah e-mailovej spravy
     * @param subject predmet spravy
     * @throws MessagingException chyba pri odosielani emailu
     */
    private static void sendMessage(String recipient,String text, String subject) throws MessagingException {
        System.out.println("prepare to send to " + recipient);

        initialize();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
        System.out.println("Sent message successfully....");
    }
}
