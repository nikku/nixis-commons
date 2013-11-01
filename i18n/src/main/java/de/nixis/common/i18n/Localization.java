package de.nixis.common.i18n;

import java.util.Locale;

/**
 *
 * @author nico.rehwaldt
 */
public class Localization {

  private static final ThreadLocal<Locale> threadLocal = new ThreadLocal<>();

  public static void set(Locale locale) {
    threadLocal.set(locale);
  }

  public static Locale get() {
    Locale locale = threadLocal.get();
    return locale != null ? locale : Locale.getDefault();
  }
}
