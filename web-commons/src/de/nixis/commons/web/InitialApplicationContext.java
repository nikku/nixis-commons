package de.nixis.commons.web;

import de.nixis.configuration.bean.Configuration;
import de.nixis.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * This is a utility class which provides methods to
 * lookup beans, the configuration and loggers for all classes
 * in this project
 * 
 * @author Dummdoedel
 */
public class InitialApplicationContext {

    private static InitialContext ix;
    private static Logger logger;

    private static Configuration configuration;
    
    /**
     * Return configuration
     * @return
     */
    public static Configuration getConfig() {
        if (configuration == null) configuration = lookupBean(Configuration.class);
        return configuration;
    }
    
    /**
     * Look up logger for specified class
     * whether or not debugging is enabled for this class depends on a
     * [full-class-name].debug property set in configuration
     * 
     * @param clazz
     * @return logger for specified class
     */
    public static Logger getLogger(Class clazz) {
        return lookupLogger(clazz, true);
    }
    
    /**
     * Get logger for this context. It is per se in debugmode, since
     * the configuration might not yet have been loaded
     * @return
     */
    private static Logger getLogger() {
        if (logger == null) logger = lookupLogger(InitialApplicationContext.class, false);
        return logger;
    }

    /**
     * Look up logger for specified class.
     * Second parameter specifies 
     * @return
     */
    private static Logger lookupLogger(Class clazz, boolean initFromConfig) {        
        boolean debug = (!initFromConfig ? true : getConfig().getProperty(clazz.getName() + ".debug", false, true));
        return new Logger(clazz, debug);
    }

    /**
     * Look up bean with the specified Interface
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> T lookupBean(Class<T> clazz) {
        
        try {
            System.out.println("MOOOOOOOOOOOOOOP " + getContext().getNameInNamespace());
            return (T) getContext().lookup("anog.small.rl/" + clazz.getSimpleName() + "/local");
        } catch (NamingException ne) {
            getLogger().fatal("Unable to load " + clazz.getSimpleName(), ne); 
            throw new RuntimeException(ne);
        } catch(ClassCastException cce) {
            getLogger().fatal("Unable to load " + clazz.getSimpleName(), cce);
            throw new RuntimeException(cce);
        }
    }

    /**
     * Get context in which the application runs
     * @return
     * @throws NamingException
     */
    private static InitialContext getContext() throws NamingException {
        if (ix == null) ix = new InitialContext();
        return ix;
    }
}
