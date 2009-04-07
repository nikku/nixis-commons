package de.nixis.commons.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dummdoedel
 */
public class Flash {
    
    private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
    private Map<String, Integer> attributesTimeToLive = new ConcurrentHashMap<String, Integer>();
    
    /**
     * Set a flash attribute
     * @param name
     * @param value
     */
    public synchronized void setAttribute(String name, Object value) {
        attributes.put(name,(Object) value);
        attributesTimeToLive.put(name, 2);
    }
    
    /**
     * Set a temporay flash attribute which is only valid in the current session
     * @param name
     * @param value
     */
    public synchronized void setTemporaryAttribute(String name, Object value) {
        attributes.put(name,(Object) value);
        attributesTimeToLive.put(name, 1);
    }

    /**
     * Get the value of a flash attribute
     * @param name
     * @return value of attribute or null
     */
    public Object getAttribute(String name) {
        return attributes.get(name);
    }
    
    /**
     * Answer whether attribute is set in flash
     * @param name
     * @return true if attribute is contained in this request
     */
    public boolean containsAttribute(String name) {
        return attributes.containsKey(name);
    }

    /**
     * Decrement time to live for all attributes
     * stored in flash
     */
    public synchronized void decrementAttributeTTL() {

        for (String name: attributes.keySet()) {
            Integer ttl = attributesTimeToLive.get(name) - 1;
            if (ttl == 0) {
                attributesTimeToLive.remove(name);
                attributes.remove(name);
            } else {
                attributesTimeToLive.put(name, ttl);
            }
        }
    }

    /**
     * Returns a string representation of this class
     * @return
     */
    @Override
    public String toString() {
        return "Flash[attributes: " + attributes.toString() + "]";
    }
}
