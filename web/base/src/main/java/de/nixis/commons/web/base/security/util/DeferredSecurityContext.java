package de.nixis.commons.web.base.security.util;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author nico.rehwaldt
 */
public abstract class DeferredSecurityContext implements SecurityContext {

  /**
   * Return the deferred security context instance to proxy requests to
   *
   * @return
   */
  protected abstract SecurityContext getDeferredInstance();

  private SecurityContext context() {
    SecurityContext c = getDeferredInstance();
    if (c == null) {
      throw new IllegalStateException("Out of context");
    }

    return c;
  }

  @Override
  public Principal getUserPrincipal() {
    return context().getUserPrincipal();
  }

  @Override
  public boolean isUserInRole(String role) {
    return context().isUserInRole(role);
  }

  @Override
  public boolean isSecure() {
    return context().isSecure();
  }

  @Override
  public String getAuthenticationScheme() {
    return context().getAuthenticationScheme();
  }
}
