package de.nixis.mail.bean;

import de.nixis.commons.ejb.GenericBean;
import de.nixis.mail.Mail;
import de.nixis.mail.util.MailingException;
import de.nixis.configuration.bean.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Dummdoedel
 */
@Stateless
public class MailerBean extends GenericBean implements Mailer {

    /**
     * Sends the specified mail using the mail relay stored in configuration.
     *
     * The mail is sent to all recipients <s>and</s> all hidden recipients
     * stored in the passed mail object.
     *
     * If no recipients are assigned to this mail a dummy recipient (for which
     * data is stored in the configuration) is created and set as the recipient.
     *
     * 
     * 
     * @param mail
     * @throws MailingException
     */
    @Override
    @SuppressWarnings("empty-statement")
    public void send(Mail mail) throws MailingException {
        
        Properties props = getMailingProperties();

        Session session;
        Message message;

        InternetAddress[] visibleRecipients = mail.getVisibleRecipients();
        InternetAddress[] recipients = mail.getRecipients();
        
        try {
            session = Session.getInstance(props);
            message = new MimeMessage(session);

            // set from
            InternetAddress from = new InternetAddress(props.getProperty("mail.content.sender-address"));
            try {
                from.setPersonal(props.getProperty("mail.content.sender-name")); }
            catch (UnsupportedEncodingException ex) {
                throw new MailingException("Sender name has unsupported encoding.", ex);
            }
            message.setFrom(from);
            
            // Set dummy recipient stored in configuration when no recipient was
            // set for this mail
            if (visibleRecipients.length == 0) {
                // This address will be shown for any recipient, it will NOT receive any message
                // @see http://www.coderanch.com/t/206725/Sockets-Internet-Protocols/JavaMail-How-to-hide-recipient
                // @see line ~79
                InternetAddress to;
                
                try {
                 to = new InternetAddress(
                        props.getProperty("mail.content.recipient-address-displacement"), 
                        props.getProperty("mail.content.recipient-name-displacement")
                      );
                } catch (UnsupportedEncodingException ex) {
                    throw new MailingException("recipient-name-displacement has unsupported encoding", ex);
                }
                message.addRecipient(RecipientType.TO, to);
            } else {
                for (InternetAddress recipient: visibleRecipients) {
                    message.addRecipient(RecipientType.TO, recipient);
                }
            }
            
            // Set content
            message.setSubject(mail.getSubject());
            message.setContent(mail.getContent(), "text/plain");
            message.setSentDate(new Date());

            message.saveChanges();
            
            Transport transport = session.getTransport("smtp");

            // Pre-send smtp login
            transport.connect(
                props.getProperty("mail.smtp.host"),
                props.getProperty("mail.smtp.authentication.user-name"),
                props.getProperty("mail.smtp.authentication.password")
            );
            
            // Adding addresses here ignores the addressee(s) set within the
            // message. That is, the mail is sent to visible and hidden
            // recipients except the dummy recipient which eventually was set.
            transport.sendMessage(message, recipients);
            transport.close();
        } catch (AddressException ex) {
            throw new MailingException(ex.getMessage());
        } catch (SendFailedException ex) {
            throw new MailingException("The message failed to send to one or more recipients", ex);
        } catch (MessagingException ex) {
            throw new MailingException(ex.getMessage());
        } 
    }

    /**
     * Return a hash map of default configuration entries to send mail to.
     * @return
     */
    private HashMap<String, String> getDefaultMailingProperties() {
        HashMap<String, String> defaults = new HashMap<String, String>();
        
        defaults.put("mail.smtp.host", "mail.nixis.de");
        defaults.put("mail.debug", "true");
        defaults.put("mail.smtp.auth", "true");
        defaults.put("mail.smtp.authentication.user-name", "mailer@nixis.de");
        defaults.put("mail.smtp.authentication.password", "premmaildelivery");
        defaults.put("mail.content.sender-address", "mailer@nixis.de");
        defaults.put("mail.content.sender-name", "Nixis Mailer");
        defaults.put("mail.content.recipient-address-displacement", "no-reply@nixis.de");
        defaults.put("mail.content.recipient-name-displacement", "Nixis Mailer");

        return defaults;
    }

    /**
     * Return a Properties object containing the information
     * needed to send mails.
     *
     * Default behaviour is to obtain data from config.
     * This may be overriden by subclasses.
     *
     * @see getDefaultMailingProperties() for avialiable properties.
     * 
     * @return Properties to use for current config
     */
    protected Properties getMailingProperties() {
        Properties props = new Properties();
        Configuration config = getConfig();

        HashMap<String, String> defaults = getDefaultMailingProperties();

        for (String name: defaults.keySet()) {
            props.setProperty(name, config.getProperty(name, defaults.get(name), true));
        }
        
        return props;
    }
}