package de.nixis.common.base.security.web;

import de.nixis.common.base.security.bean.UserLoginManager;
import java.io.IOException;

import de.nixis.common.base.security.util.DeferredSecurityContext;
import de.nixis.common.base.security.util.SimpleSecurityContext;
import de.nixis.common.base.model.UserBase;
import de.nixis.common.base.security.SecurityContextHolder;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import javax.ws.rs.ext.Provider;


/**
 *
 * @author nico.rehwaldt
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter, ContainerResponseFilter {

  public static final String CURRENT_USER_ATTR = "currentUser";

  private static final Logger LOGGER = Logger.getLogger(SecurityFilter.class.getName());

  @Inject
  private javax.inject.Provider<HttpServletRequest> httpRequestProvider;

  @Inject
  private UserLoginManager loginManager;

  @Override
  public void filter(ContainerRequestContext context) throws IOException {

    HttpServletRequest httpRequest = httpRequestProvider.get();

    UserBase user = (UserBase) httpRequest.getSession().getAttribute(CURRENT_USER_ATTR);
    if (user == null) {
      Cookie cookie = context.getCookies().get("uid");
      if (cookie != null) {
        user = loginManager.loginViaAuthToken(cookie.getValue());

        if (user != null) {
          LOGGER.fine("Logged in current user via session");
          httpRequest.getSession().setAttribute(CURRENT_USER_ATTR, user);
        }
      }
    }

    httpRequest.setAttribute(CURRENT_USER_ATTR, user);

    SecurityContextHolder.setContext(new SimpleSecurityContext(user));

    // Set request's security context
    context.setSecurityContext(new DeferredSecurityContext() {
      @Override
      protected SecurityContext getDeferredInstance() {
        return SecurityContextHolder.getContext();
      }
    });
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    SecurityContextHolder.setContext(null);
  }
}
