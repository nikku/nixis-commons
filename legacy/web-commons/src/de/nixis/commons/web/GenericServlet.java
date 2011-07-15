package de.nixis.commons.web;

import de.nixis.commons.web.*;
import de.nixis.configuration.bean.Configuration;
import de.nixis.configuration.util.Configurable;
import de.nixis.logging.Logger;
import de.nixis.logging.LoggingSupporting;
import javax.servlet.http.HttpServlet;

/**
 * Base class for all servlets in VB-project
 * @author Muffelbox
 */
public class GenericServlet extends HttpServlet implements Configurable, LoggingSupporting {
    
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
    
    /**
     * Look up bean with the specified Interface
     * @param <T>
     * @param clazz
     * @return
     */
    protected <T> T lookupBean(Class<T> clazz) {
        return InitialApplicationContext.lookupBean(clazz);
    }
}
