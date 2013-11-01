package de.nixis.common.validation;

import java.util.Locale;
import javax.validation.MessageInterpolator;

import de.nixis.common.i18n.Localization;

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
    return delegate.interpolate(message, cntxt, Localization.get());
  }

  @Override
  public String interpolate(String message, Context cntxt, Locale locale) {
    return delegate.interpolate(message, cntxt, locale);
  }
}
