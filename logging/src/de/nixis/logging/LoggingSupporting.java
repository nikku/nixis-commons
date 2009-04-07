package de.nixis.logging;

/**
 * Interface for all classes which are able to obtain a
 * logger which can be used to ... log
 * 
 * @author Dummdoedel
 */
public interface LoggingSupporting {
    /**
     * Obtain logger via this method
     * 
     * @return
     */
    public de.nixis.logging.Logger getLogger();
}
