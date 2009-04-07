package de.nixis.configuration.util;

import de.nixis.configuration.bean.Configuration;

/**
 * Interface for all classes which are somehow configurable,
 * which means, that the configuration can be obtained
 * 
 * @author Dummdoedel
 */
public interface Configurable {

    /**
     * Get configuration for vb project
     * @return
     */
    public Configuration getConfig();
}