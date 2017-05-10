
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Stateless
public class MailSender {

    private static final Logger logger = LogManager.getLogger(MailSender.class);

    public void init() {

    }

    public void sendMail(String name, String lastname, String from, String to, String text, String host, Integer port, String login, String pass) {
        if (from != null && to != null && text != null) {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "false");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "host");
            properties.put("mail.smtp.port", "port");
            Session session = Session.getInstance(properties);
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject("Test message from " + name + " " + lastname);
                message.setText(text);
                Transport transport = session.getTransport("smtp");
                transport.connect(host, port, login, pass);
                if (transport.isConnected()) {
                    transport.sendMessage(message, message.getAllRecipients());
                    logger.info("Message sent to " + to);
                }
                transport.close();
            } catch (AddressException e) {
                logger.error("Incorrect address", e);
            } catch (MessagingException e) {
                logger.error("Error sending message", e);
            }
        }
    }
}
