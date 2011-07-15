package de.nixis.commons.ejb;

import de.nixis.configuration.bean.Configuration;
import de.nixis.configuration.util.Configurable;
import de.nixis.logging.Logger;
import de.nixis.logging.LoggingSupporting;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Base class for all beans around. Has logging support and is configurable.
 * 
 * @author Muffelbox, Dummdoedel
 */
public class GenericBean implements Configurable, LoggingSupporting {

    @EJB
    private Configuration config;
    
    private Logger logger;

    @PersistenceContext
    protected EntityManager em;
    
    /**
     * Return configuration
     * @return
     */
    @Override
    public Configuration getConfig() {
        return config;
    }
    
    /**
     * Return logger
     * @return
     */
    @Override
    public Logger getLogger() {
        if (logger == null) logger = lookupLogger();
        return logger;
    }
    
    /**
     * Look up logger
     * @return
     */
    private Logger lookupLogger() {
        return new Logger(getClass(), getConfig().getProperty(getClass().getName() + ".debug", false, true));
    }
}