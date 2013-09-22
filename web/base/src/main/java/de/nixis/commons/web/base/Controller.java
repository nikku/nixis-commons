/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base;

import de.nixis.commons.web.base.model.UserBase;
import de.nixis.commons.web.base.security.SecurityContextHolder;
import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.server.mvc.Viewable;

/**
 * Provides basic controller functionality.
 *
 * @author markus.goetz
 * @author nico.rehwaldt
 */
public abstract class Controller {

  /**
   * Return a response builder to build a redirect response
   *
   * @param path to redirect the response to
   * @return
   */
  protected Response redirect(String path) {
    URI redirectURL = UriBuilder.fromPath(path).build();
    return Response.seeOther(redirectURL).build();
  }

  /**
   * Return the user active in the current security context or
   * <code>null</code> if the security context is not set or no current user exists.
   *
   * @return the active user or null
   */
  protected UserBase getActiveUser() {
    SecurityContext context = SecurityContextHolder.getContext();
    if (context != null) {
      return (UserBase) context.getUserPrincipal();
    } else {
      return null;
    }
  }

  /**
   * Render the specified template
   *
   * @param path path to template inside model folder
   * @param model model object to pass to template
   * @return template view
   */
  protected Viewable render(String path, Object model) {
    return new Viewable(path, model);
  }

  /**
   * Render the specified template without a model instance
   *
   * @param path
   * @return
   */
  protected Viewable render(String path) {
    return render(path, null);
  }
}