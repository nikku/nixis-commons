package de.nixis.commons.web.flash;

import java.util.Map;

/**
 * Holds flash data in a thread local.
 * 
 * @author nico.rehwaldt
 */
class FlashHolder {

  private static final ThreadLocal<Map<String, Object>> flashThreadLocal = new ThreadLocal<Map<String, Object>>();

  public static Map<String, Object> get() {
    return flashThreadLocal.get();
  }

  public static void set(Map<String, Object> map) {
    flashThreadLocal.set(map);
  }
}
