package de.nixis.common.base.web;

import de.nixis.common.base.model.UserBase;
import de.nixis.common.base.security.SecurityContextHolder;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.mvc.Viewable;

/**
 * Provides basic controller functionality.
 *
 * @author markus.goetz
 * @author nico.rehwaldt
 */
public abstract class Controller {

  @Inject
  private Provider<UriInfo> uriInfoProvider;

  /**
   * Return a response builder to build a redirect response
   *
   * @param path to redirect the response to
   * @return
   */
  protected Response redirect(String path) {
    return redirectBuilder(path).build();
  }

  protected ResponseBuilder redirectBuilder(String path) {

    UriInfo uriInfo = getUriInfo();
    UriBuilder uriBuilder;

    if (uriInfo == null) {
      uriBuilder = UriBuilder.fromPath(path);
    } else {
      if (path.contains("://")) {
        uriBuilder = UriBuilder.fromUri(path);
      } else if (path.startsWith("/")) {
        uriBuilder = uriInfo.getBaseUriBuilder().path(path);
      } else {
        uriBuilder = uriInfo.getRequestUriBuilder().path("../" + path);
      }
    }

    return Response.seeOther(uriBuilder.build());
  }

  protected UriInfo getUriInfo() {
    // may return null during mocked tests
    return uriInfoProvider != null ? uriInfoProvider.get() : null;
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

  protected Response ok() {
    return Response.ok().build();
  }

  protected Map<String, Object> none() {
    return Collections.EMPTY_MAP;
  }
}