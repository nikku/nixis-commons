package de.nixis.configuration.bean;

import de.nixis.configuration.domain.ConfigEntry;
import de.nixis.configuration.util.ConfigurationException;
import de.nixis.configuration.util.ConfigurationChangeListener;

import java.util.List;
import javax.ejb.Local;

/**
 * Interface for bean which is able to add, delete and alter configuration
 * entries
 * 
 * @author Dummdoedel
 */
@Local
public interface Configuration {

    public List<ConfigEntry> getConfiguration();

    public ConfigEntry getEntry(Long id);
    public ConfigEntry getEntry(String key) throws ConfigurationException;

    public void deleteEntry(String key);
    public void deleteEntry(Long id);

    public void addEntry(String key, Object value) throws ConfigurationException;
    public void addEntry(ConfigEntry entry) throws ConfigurationException;

    public void updateEntry(String key, Object value) throws ConfigurationException;
    public void updateEntry(Long id, Object value) throws ConfigurationException;

    public long getProperty(String key, long defaultValue);
    public boolean getProperty(String key, boolean defaultValue);
    public String getProperty(String key, String defaultValue);
    public long getProperty(String key, long defaultValue, boolean insertIfNotExists);
    public boolean getProperty(String key, boolean defaultValue, boolean insertIfNotExists);
    public String getProperty(String key, String defaultValue, boolean insertIfNotExists);

    public void setProperty(String key, long value);
    public void setProperty(String key, boolean value);
    public void setProperty(String key, String value);

    public void addChangeListener(ConfigurationChangeListener listener);
    public void removeChangeListener(ConfigurationChangeListener listener);
    public void notifyChangeListeners();
}