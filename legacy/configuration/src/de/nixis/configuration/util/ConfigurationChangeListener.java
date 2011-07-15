package de.nixis.configuration.util;

/**
 * Interface for classes which recieve notifications on configuration changes
 * 
 * @author Dummdoedel
 */
public interface ConfigurationChangeListener {

    /**
     * Configuration changed. Do something.
     */
    public void configurationChanged();
}
