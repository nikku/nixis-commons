package de.nixis.mail.util;

import javax.ejb.EJBException;

/**
 * Exception indicating that something went wrong while sending mails.
 * 
 * @author Dummdoedel
 */
public class MailingException extends EJBException {

    public static final long serialVersionUID = 21321323;

    /**
     * Creates a new MailingException with the given cause
     * 
     * @param message
     */
    public MailingException(String message) {
        super(message);
    }

    /**
     * Creates a new instance from the given cause
     * 
     * @param ex
     */
    public MailingException(Exception ex) {
        super(ex);
    }

    /**
     * Creates a new instance from the given message and cause
     * @param message
     * @param ex
     */
    public MailingException(String message, Exception ex) {
        super(message, ex);
    }
}
