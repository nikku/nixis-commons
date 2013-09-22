/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base.security;

import java.io.IOException;
import de.nixis.commons.web.base.security.util.DeferredSecurityContext;
import de.nixis.commons.web.base.security.util.SimpleSecurityContext;
import de.nixis.commons.web.base.model.UserBase;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.container.ContainerRequestFilter;

import javax.ws.rs.ext.Provider;

/**
 *
 * @author nico.rehwaldt
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

  private static final Logger LOGGER = Logger.getLogger(SecurityFilter.class.getName());

  @Context
  private HttpServletRequest httpRequest;

  @Inject
  private UserLoginManager loginManager;

  @Override
  public void filter(ContainerRequestContext context) throws IOException {

    UserBase user = (UserBase) httpRequest.getSession().getAttribute("currentUser");
    if (user == null) {
      Cookie cookie = context.getCookies().get("uid");
      if (cookie != null) {
        user = loginManager.loginViaAuthToken(cookie.getValue());

        if (user != null) {
          httpRequest.setAttribute("currentUser", user);
          LOGGER.fine("Logged in current user via session");
        }
      }
    }

    SecurityContextHolder.setContext(new SimpleSecurityContext(user));

    // Set request's security context
    context.setSecurityContext(new DeferredSecurityContext() {
      @Override
      protected SecurityContext getDeferredInstance() {
        return SecurityContextHolder.getContext();
      }
    });
  }
}
