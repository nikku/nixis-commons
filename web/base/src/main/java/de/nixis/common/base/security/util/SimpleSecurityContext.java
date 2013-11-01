/*
 * Part of nixis.commons
 */
package de.nixis.common.base.security.util;

import de.nixis.common.base.model.UserBase;
import javax.ws.rs.core.SecurityContext;

/**
 * Simple security context implementation
 *
 * @author nico.rehwaldt
 */
public class SimpleSecurityContext implements SecurityContext {

  private UserBase user;

  public SimpleSecurityContext() {
    this(null);
  }

  public SimpleSecurityContext(UserBase user) {
    this.user = user;
  }

  @Override
  public UserBase getUserPrincipal() {
    return user;
  }

  @Override
  public boolean isUserInRole(String role) {
    UserBase principal = getUserPrincipal();
    return principal != null && principal.hasRole(role);
  }

  @Override
  public boolean isSecure() {
    return false;
  }

  @Override
  public String getAuthenticationScheme() {
    return FORM_AUTH;
  }
}
