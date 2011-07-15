package de.nixis.logging;

/**
 * Basic class which is used for logging
 * @author Dummdoedel
 */
public class Logger {
    
    private org.jboss.logging.Logger logger;
    private boolean debug;
    
    public Logger(String ident) {
        this.logger = org.jboss.logging.Logger.getLogger(ident);
        this.debug = false;
    }
    
    public Logger(Class clazz) {
        this.logger = org.jboss.logging.Logger.getLogger(clazz);
        this.debug = false;
    }
    
    public Logger(String ident, boolean debug) {
        this.logger = org.jboss.logging.Logger.getLogger(ident);
        this.debug = debug;
    }
    
    public Logger(Class clazz, boolean debug) {
        this.logger = org.jboss.logging.Logger.getLogger(clazz);
        this.debug = debug;
    }
    
    public void debug(Object message) {
        if (debug) logger.debug(message);
    }
    
    public void debug(Object message, Throwable t) {
        if (debug) logger.debug(message, t);
    }
    
    public void info(Object message) {
        if (debug) logger.info(message);
    }
    
    public void info(Object message, Throwable t) {
        if (debug) logger.info(message, t);
    }

    public void warn(Object message) {
        logger.warn(message);
    }
    
    public void warn(Object message, Throwable t) {
        logger.warn(message, t);
    }
    
    public void error(Object message) {
        logger.error(message);
    }
    
    public void error(Object message, Throwable t) {
        logger.error(message, t);
    }
    
    public void fatal(Object message) {
        logger.fatal(message);
    }
    
    public void fatal(Object message, Throwable t) {
        logger.fatal(message, t);
    }
}
