/*
 * Part of de.nixis.commons
 */
package de.nixis.commons.i18n;

import java.util.Locale;

/**
 * Super simple localization interface which allows to store the
 * current locale in a thread local variable
 * 
 * @author nico.rehwaldt
 */
public class Localization {
    
    private static final ThreadLocal<Locale> variable = new ThreadLocal<Locale>();
    
    public static void setLocale(Locale locale) {
        variable.set(locale);
    }
    
    public static Locale getLocale() {
        return variable.get();
    }
}
