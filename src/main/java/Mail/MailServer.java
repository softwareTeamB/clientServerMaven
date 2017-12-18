package Mail;

import global.ConsoleColor;
import global.LoadPropFile;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.*;

/**
 * Klasse de de mail maakt en verstuurd
 *
 * @author michel
 */
public class MailServer {

    /*
     * Boolean mailler. Boolean is standaart op false gezet
     */
    private static boolean sendMail = false;

    private static Properties mailServerProperties;
    private static Session getMailSession;
    private static MimeMessage generateMailMessage;
    private Properties loadConfigProp;
    private Properties mailProperties;

    /**
     * Constructor
     */
    public MailServer() {
        try {
            this.loadConfigProp = LoadPropFile.loadPropFile("config");
            this.mailProperties = LoadPropFile.loadPropFile("mailAccount");
            
            //update boolean
            sendMail = true;
            
            
        } catch (IOException ex) {
            ConsoleColor.err("Er is een error in de mailServer constructor. Dit is de error " + ex
                    + ". Het systeem wordt afgesloten.");
            System.exit(0);
        }

    }

    /**
     * Methoden die de mail stuurt
     *
     * @param mailAddress het mail address
     * @param emailBody mail body
     * @throws AddressException throw error
     * @throws MessagingException bericht error
     */
    public void generateAndSendEmail(String mailAddress, String emailBody) throws AddressException, MessagingException {
        
        //kijk of het mogelijk is om een mail te sturen
        if(!sendMail){
            ConsoleColor.warn("Het is niet mogelijk om een mail te sturen.");
        
            //stop de methoden
            return;
        }
        
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailProperties.getProperty("googleAccount"),
                        mailProperties.getProperty("googlePassword"));
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("cloudminingnl@gmail.com"));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("cloudminingnl@gmail.com"));

            // Set Subject: header field
            message.setSubject("Trading bot");

            // Send the actual HTML message, as big as you like
            message.setContent(emailBody, "text/html");

            // Send message
            Transport.send(message);
            ConsoleColor.out("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            
            //zet send boolean op false
            sendMail = false;
        }
    }

}
