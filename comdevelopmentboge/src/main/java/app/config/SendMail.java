package app.config;


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
     * E-mailova adresa odosielateľa
     */
    private static final String from = "resetheslaboge@gmail.com";
    private static final String password = "hesloboge";
    private static final String host = "smtp.gmail.com";

    private static Properties props;
    private static Session session;

    private static void initialize() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
    }

    /**
     * Pošle e-mailovú správu s vygenerovaným heslom
     * @param recipient e-mailova adresa príjmateľa (musí byť Gmail)
     * @param newPassword vygenerované heslo
     * @throws MessagingException
     */
    public static void sendNewPassword(String recipient, String newPassword) throws MessagingException {
        sendMessage(recipient,"Nové vygenerované heslo je: " + newPassword,"Reset hesla");

    }

    /**
     * Pošle notifikáciu na e-mailovú adresu
     * @param recipient e-mailova adresa príjmateľa (musí byť Gmail)
     * @param text obsah e-mailu
     * @throws MessagingException
     */
    public static void sendReminder(String recipient,String text) throws MessagingException {
        sendMessage(recipient,text,"Riziko prekročenia plánovaných nákladov");
    }

    /**
     * Pošle e-mailovú správu na základe parametrov
     * @param recipient e-mailova adresa príjmateľa (musí byť Gmail)
     * @param text obsah e-mailovej správy
     * @param subject predmet správy
     * @throws MessagingException
     */
    private static void sendMessage(String recipient,String text, String subject) throws MessagingException {
        System.out.println("prepare to send");

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
