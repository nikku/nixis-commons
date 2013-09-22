package de.nixis.commons.web.flash;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nico.rehwaldt
 */
public class SimpleFlash implements Flash {

  private final HashMap<String, Object> map;

  public SimpleFlash() {
    this.map = new HashMap<String, Object>();
  }

  @Override
  public void put(String name, Object object) {
    map.put(name, object);
  }

  @Override
  public Object get(String name) {
    return map.get(name);
  }

  @Override
  public void putAll(Map<String, Object> values) {
    map.putAll(values);
  }

  @Override
  public void clear() {
    map.clear();
  }
}
