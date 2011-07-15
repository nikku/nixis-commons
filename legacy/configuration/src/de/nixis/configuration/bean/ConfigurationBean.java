package de.nixis.configuration.bean;

import de.nixis.configuration.domain.ConfigEntry;

import de.nixis.configuration.util.ConfigurationChangeListener;
import de.nixis.configuration.util.ConfigurationException;
import de.nixis.logging.Logger;
import de.nixis.logging.LoggingSupporting;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 * The configuration bean providing an interface for storing and
 * retrieving configuration entries.
 *
 * @author Dummdoedel, Muffelbox
 */
@Stateless
public class ConfigurationBean implements Configuration, LoggingSupporting {

    private static HashSet<ConfigurationChangeListener> listeners;

    @PersistenceContext
    private EntityManager em;

    private Logger logger;

    /**
     * Returns all entries.
     * @return a list with all configuration entries
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigEntry> getConfiguration() {
        return (List<ConfigEntry>) em.createNamedQuery("ConfigEntry.getAll").getResultList();
    }

    /**
     * Returns a configuration entry.
     * 
     * @param key The key's name.
     * @return The value.
     * @throws ConfigurationException In case the key does not exist.
     */
    @Override
    public ConfigEntry getEntry(String key) throws ConfigurationException {
        try {
            return (ConfigEntry) em.createNamedQuery("ConfigEntry.getByKey").setParameter("key", key).getSingleResult();

        } catch (NoResultException e) {
            throw new ConfigurationException("Config entry[key='" + key + "'] was not found.", e);
        } catch (NonUniqueResultException e) {
            return null; // can not occure
        }
    }

    /**
     * Overwrites an existing entry.
     *
     * @param key The key
     * @param value The value
     * @throws ConfigurationException if entry does not exists.
     */
    @Override
    public void updateEntry(String key, Object value) throws ConfigurationException {
        try {
            ConfigEntry entry = getEntry(key);
            entry.setValue(convertObjectToString(value));
        } catch (ConfigurationException e) {
            throw new ConfigurationException("Could not update entry[key='" + key + "']. " + e.getMessage(), e);
        }
    }

    /**
     * Update entry with specified id to hold the specified value
     *
     * @param id
     * @param value
     *
     * @throws ConfigurationException if entry was not found
     */
    public void updateEntry(Long id, Object value) throws ConfigurationException {
        try {
            ConfigEntry entry = getEntry(id);
            entry.setValue(convertObjectToString(value));
        } catch (ConfigurationException e) {
            throw new ConfigurationException("Could not update entry[id='" + id + "']. " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an entry.
     *
     * @param key the name
     */
    @Override
    public void deleteEntry(String key) {
        try {
            em.remove(getEntry(key));
        } catch (ConfigurationException ex) {
            ;
        }
    }

    /**
     * Deletes an entry.
     * 
     * @param id
     */
    public void deleteEntry(Long id) {
        try {
            em.remove(getEntry(id));
        } catch (ConfigurationException ex) {
            ;
        }
    }

    /**
     * Adds an entry.
     * 
     * @param key the key as String.
     * @param value the value as Object. toString-method used to get configuration data.
     * @throws ConfigurationException if no toString-method is
     *          available or the key already exists.
     */
    @Override
    public void addEntry(String key, Object value) throws ConfigurationException {
        addEntry(new ConfigEntry(key, convertObjectToString(value)));
    }

    /**
     * Adds an entry.
     * 
     * @param entry the ConfigEntry.
     * @throws ConfigurationException if entry allready exists.
     */
    @Override
    public void addEntry(ConfigEntry entry) throws ConfigurationException {
        try {
            em.persist(entry);
            getLogger().debug("Entry[key='" + entry.getKey() + "'] added");
        } catch (EntityExistsException e) {
            getLogger().warn("Error adding entry[key='" + entry.getKey() + "']. It already exists.");
        }
    }

    /**
     * Get long property
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public long getProperty(String key, long defaultValue) {
        return Long.parseLong(getProperty(key, Long.toString(defaultValue), false));
    }

    /**
     * Get long property
     * 
     * @param key
     * @param defaultValue
     * @param insertIfNotExists
     * @return
     */
    public long getProperty(String key, long defaultValue, boolean insertIfNotExists) {
        return Long.parseLong(getProperty(key, Long.toString(defaultValue), insertIfNotExists));
    }

    /**
     * Get boolean property.
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getProperty(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getProperty(key, Boolean.toString(defaultValue), false));
    }

    /**
     * Get boolean property.
     *
     * @param key
     * @param defaultValue
     * @param insertIfNotExists
     * @return
     */
    public boolean getProperty(String key, boolean defaultValue, boolean insertIfNotExists) {
        return Boolean.parseBoolean(getProperty(key, Boolean.toString(defaultValue), insertIfNotExists));
    }

    /**
     * Get string property.
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        return getProperty(key, defaultValue, false);
    }

    /**
     * Get string property.
     *
     * Read a property out of the database and return its value.
     * <s>insertIfNotExists</s> specifies whether the default data should be
     * inserted in db if it is missing
     * 
     * @param key
     * @param defaultValue
     * @param insertIfNotExists
     * @return
     */
    public String getProperty(String key, String defaultValue, boolean insertIfNotExists) {
        try {
            return getEntry(key).getValue();
        } catch (Exception e) {
            if (insertIfNotExists) {
                setProperty(key, defaultValue);
            }
            return defaultValue;
        }
    }

    /**
     * Set numeric property
     * 
     * @param key
     * @param value
     */
    public void setProperty(String key, long value) {
        setProperty(key, Long.toString(value));
    }

    /**
     * Set boolean property
     * 
     * @param key
     * @param value
     */
    public void setProperty(String key, boolean value) {
        setProperty(key, Boolean.toString(value));
    }

    /**
     * Set string property
     * 
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        getLogger().debug("ConfigEntry set property entry[key='" + key + "']");

        ConfigEntry entry;
        try {
            entry = getEntry(key);
            entry.setValue(value);

            em.merge(entry);
        } catch (ConfigurationException e) {
            try {
                getLogger().debug("ConfigEntry entry[key='" + key + "'] not found. Creating new one.");
                entry = new ConfigEntry(key, value, true);
                addEntry(entry);
            } catch (ConfigurationException ex) {
                getLogger().error("Failed to add config entry[key='" + key + "']", ex);
            }
        }
    }

    /**
     * Convert object to string
     * 
     * @param value
     * @return
     *
     * @throws ConfigurationException
     */
    private static final String convertObjectToString(Object value) {
        try {
            Method m = value.getClass().getMethod("toString");
            return (String) m.invoke(value);
        } catch (Exception ex) { // there may be several exceptions ;-)
            throw new ConfigurationException("Only objects implementing a toString()-method are supported.", ex);
        }
    }

    /**
     * Return ConfigEntry with given id or null if entry was not found
     * 
     * @param id
     * @return
     */
    public ConfigEntry getEntry(Long id) {
        return em.find(ConfigEntry.class, id);
    }

    /**
     * Return logger for this configuration
     * @return
     */
    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = new Logger(this.getClass());
        }

        return this.logger;
    }

    /**
     * Add change listener.
     * 
     * @param listener
     */
    public void addChangeListener(ConfigurationChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove change listener.
     * 
     * @param listener
     */
    public void removeChangeListener(ConfigurationChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify change listeners that database changes occured.
     */
    public void notifyChangeListeners() {
        for (ConfigurationChangeListener l : listeners) {
            l.configurationChanged();
        }
    }
}
