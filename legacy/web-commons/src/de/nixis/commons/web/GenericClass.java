package de.nixis.commons.web;

import de.nixis.configuration.bean.Configuration;
import de.nixis.configuration.util.Configurable;
import de.nixis.logging.Logger;
import de.nixis.logging.LoggingSupporting;

/**
 *
 * @author Dummdoedel
 */
public class GenericClass implements Configurable, LoggingSupporting {
    
    private Logger logger;
    
    /**
     * Return configuration
     * @return
     */
    @Override
    public Configuration getConfig() {
        return InitialApplicationContext.getConfig();
    }

    /**
     * Return logger
     * @return
     */
    @Override
    public Logger getLogger() {
        if (logger == null) logger = InitialApplicationContext.getLogger(getClass());
        return logger;
    }
}
