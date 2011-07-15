package de.nixis.mail.bean;

import de.nixis.mail.Mail;
import de.nixis.mail.util.MailingException;

/**
 * Interface to mail sending bean
 * @author Dummdoedel
 */
public interface Mailer {

    /**
     * Sends mail
     * 
     * @param mail
     * @throws MailingException
     */
    public void send(Mail mail) throws MailingException;
}