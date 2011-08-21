/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base.flash;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author nico.rehwaldt
 */
public class FlashMap implements Map<String, Object> {
    
    private final HashMap<String, Object> map = new HashMap<String, Object>();

    /**
     * Get values
     * @return
     */
    public Map<String, Object> getRequestAttributes() {
        Map<String, Object> result = new HashMap<String, Object>(map);
        synchronized (map) {
            map.clear();
        }
        return result;
    }
    
    @Override
    public int size() {
        synchronized(map) {
            return map.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized(map) {
            return map.isEmpty();
        }
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean containsKey(Object key) {
        synchronized(map) {
            return map.containsKey(key);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        synchronized(map) {
            return map.containsValue(value);
        }
    }

    @Override
    public Object get(Object key) {
        synchronized(map) {
            return map.get(key);
        }
    }

    @Override
    public Object put(String key, Object value) {
        synchronized(map) {
            return map.put(key, value);
        }
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public Object remove(Object key) {
        synchronized(map) {
            return map.remove(key);
        }
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        throw new UnsupportedOperationException("Not supported by flash map");
    }

    @Override
    public void clear() {
        synchronized(map) {
            map.clear();
        }
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        synchronized(map) {
            return map.entrySet();
        }
    }
}
