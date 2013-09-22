package de.nixis.commons.web.flash;

import java.util.HashMap;
import java.util.Map;

/**
 * An interface for the flash (i.e. in-between two requests) context.
 *
 * @author nico.rehwaldt
 */
public interface Flash {

  /**
   * Puts an element with the given name in the
   * flash context.
   *
   * @param name
   * @param object
   */
  public void put(String name, Object object);

  /**
   * Returns an already set element.
   *
   * @param name
   * @return
   */
  public Object get(String name);

  /**
   * Puts all elements in the given map into the
   * flash context.
   *
   * @param values
   */
  public void putAll(Map<String, Object> values);

  /**
   * Clears the flash context, removing all elements stored in it
   */
  public void clear();
}
