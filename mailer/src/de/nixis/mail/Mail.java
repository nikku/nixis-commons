package de.nixis.mail;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Wrapper Object for some mail related stuff nobody
 * wants to worry about
 * 
 * @author Dummdoedel
 */
public class Mail {

    private String subject;
    private String content;

    private List<InternetAddress> recipients;
    private List<InternetAddress> hiddenRecipients;

    /**
     * Constructs a new mail instance from the given subject,
     * content and the list of recipients
     * 
     * @param subject
     * @param content
     * @param recipients
     * @throws AddressException if recipients contains a malformed email address
     */
    public Mail(String subject, String content, List<String> recipients) throws AddressException {
        this(subject, content);

        addRecipients(recipients);
    }

    /**
     * Constructs a new mail instance from the given subject,
     * content and a single recipient
     * @param subject
     * @param content
     * @param recipient
     * @throws AddressException if recipient is a malformed email address
     */
    public Mail(String subject, String content, String recipient) throws AddressException {
        this(subject, content);

        addRecipient(recipient);
    }

    /**
     * Constructs a new mail instance from the given subject,
     * content and a single recipient
     * 
     * @param subject
     * @param content
     * @param recipient
     * @throws AddressException if recipient is a malformed email address
     */
    public Mail(String subject, String content, InternetAddress recipient) {
        this(subject, content);

        addRecipient(recipient);
    }

    /**
     *
     * @param subject
     * @param content
     */
    public Mail(String subject, String content) {
        if (subject == null) {
            throw new IllegalArgumentException("Argument subject may not be null");
        }
        if (content == null) {
            throw new IllegalArgumentException("Argument content may not be null");
        }

        this.subject = subject;
        this.content = content;

        this.recipients = new ArrayList<InternetAddress>();
        this.hiddenRecipients = new ArrayList<InternetAddress>();
    }
    
    /**
     * Add a recipient to this mail
     * @param recipient
     * @throws AddressException if recipient is a malformed email address
     */
    public void addRecipient(String recipient) throws AddressException {
        addRecipient(new InternetAddress(recipient));
    }

    /**
     * Add a recipient to this mail
     * @param recipient
     */
    public void addRecipient(InternetAddress recipient) {
        recipients.add(recipient);
    }

    /**
     * Add a recipient to this mail
     * @param recipient
     * @throws AddressException if recipient is a malformed email address
     */
    public void addRecipient(String recipient, String displayName) throws AddressException {
        recipients.add(new InternetAddress(recipient));
    }

    /**
     * Add a hidden recipient to this mail
     * @param recipient
     * @throws AddressException if recipient is a malformed email address
     * @throws UnsupportedEncodingException if displayName has unsupported encoding
     */
    public void addHiddenRecipient(String recipient, String displayName) throws AddressException, UnsupportedEncodingException {
        hiddenRecipients.add(new InternetAddress(recipient, displayName));
    }

    /**
     * Add a hidden recipient to this mail
     * @param recipient
     * @throws AddressException if recipient is a malformed email address
     */
    public void addHiddenRecipient(String recipient) throws AddressException {
         hiddenRecipients.add(new InternetAddress(recipient));
    }

    /**
     * Add a hidden recipient to this mail
     * 
     * @param recipient
     */
    public void addHiddenRecipient(InternetAddress recipient) {
         hiddenRecipients.add(recipient);
    }

    /**
     * Add a collection of recipients to this mail
     * @param recipients
     * @throws AddressException if recipients contains a malformed email address
     */
    public void addRecipients(Collection<String> recipients) throws AddressException {
        for (String recipient : recipients) {
            addRecipient(recipient);
        }
    }

    /**
     * Answer recipients visible in the mail TO field.
     * @return
     */
    public InternetAddress[] getVisibleRecipients() {
        return recipients.toArray(new InternetAddress[0]);
    }

    /**
     * Answer the hidden recipients
     * @return
     */
    public InternetAddress[] getHiddenRecipients() {
        return hiddenRecipients.toArray(new InternetAddress[0]);
    }

    /**
     * Answer recipients visible in the mail TO field.
     * 
     * @return
     */
    public InternetAddress[] getRecipients() {
        ArrayList<InternetAddress> allRecipients = new ArrayList<InternetAddress>(recipients);
        allRecipients.addAll(hiddenRecipients);

        return allRecipients.toArray(new InternetAddress[0]);
    }
    
    /**
     * Return the subject 
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Return the content of this mail
     * @return
     */
    public String getContent() {
        return content;
    }
}
