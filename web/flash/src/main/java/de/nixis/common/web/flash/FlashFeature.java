package de.nixis.common.web.flash;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * A {@link Feature} that provides a generic {@link Flash} object.
 *
 * @author nico.rehwaldt
 */
@ConstrainedTo(RuntimeType.SERVER)
public class FlashFeature implements Feature {

  @Override
  public boolean configure(FeatureContext context) {

    if (!context.getConfiguration().isRegistered(FlashInitializingFilter.class)) {
      context.register(FlashInitializingFilter.class);
      context.register(FlashSessionBindingFilter.class);
      context.register(new FlashBinder());

      return true;
    }

    return false;
  }
}
