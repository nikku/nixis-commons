/*
 * Part of de.nixis.commons
 */
package de.nixis.commons.validation;

import de.nixis.commons.i18n.Localization;
import java.util.Locale;
import javax.validation.MessageInterpolator;

/**
 *
 * @author nico.rehwaldt
 */
public class ContextualMessageInterpolator implements MessageInterpolator {
    
    private MessageInterpolator delegate;
    
    public ContextualMessageInterpolator(MessageInterpolator delegate) {
        this.delegate = delegate;
    }

    @Override
    public String interpolate(String message, Context cntxt) {
        Locale locale = Localization.getLocale();
        if (locale == null) {
            locale = Locale.getDefault();
        }
        
        return delegate.interpolate(message, cntxt, locale);
    }

    @Override
    public String interpolate(String message, Context cntxt, Locale locale) {
        return delegate.interpolate(message, cntxt, locale);
    }
}
