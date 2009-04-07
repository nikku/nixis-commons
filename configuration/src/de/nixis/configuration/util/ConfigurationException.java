package de.nixis.configuration.util;

import javax.ejb.EJBException;

/**
 * Exception indicating a misconfiguration
 * @author Dummdoedel
 */
public class ConfigurationException extends EJBException {

    public static final long serialVersionUID = 213321238321123l;
    
    /**
     * Creates a new exception with the given message
     * @param message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the given nested exception
     * @param exception
     */
    public ConfigurationException(Exception exception) {
        super(exception);
    }

    /**
     * Creates a new exception with the given cause and nested exception
     * @param message
     * @param exception
     */
    public ConfigurationException(String message, Exception exception) {
        super(message, exception);
    }
}
