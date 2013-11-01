package de.nixis.common.web.flash;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;


/**
 * Filter which initializes the flash context in an application
 *
 * @author nico.rehwaldt
 */
@Provider
@Singleton
public class FlashSessionBindingFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final String FLASH_SESSION_ATTR = FlashSessionBindingFilter.class.getName() + "__FLASH";

  @Inject
  private javax.inject.Provider<Flash> flashProvider;

  @Inject
  private javax.inject.Provider<HttpServletRequest> httpServletRequestProvider;

  @Override
  public void filter(ContainerRequestContext context) throws IOException {
    loadFlash(request());
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    Flash flash = flash();

    if (flash != null) {
      Map<String, Object> flashMap = flash.drain();
      storeFlash(flashMap, request());
    }
  }

  protected Map<String, Object> getAndRemoveSessionFlash(HttpServletRequest request) {
    HttpSession session = request.getSession();
    if (session == null) {
      return null;
    }

    Map<String, Object> flash = (Map<String, Object>) session.getAttribute(FLASH_SESSION_ATTR);
    session.removeAttribute(FLASH_SESSION_ATTR);
    return flash;
  }

  protected void setRequestAttributes(Map<String, Object> flash, HttpServletRequest request) {
    for (Map.Entry<String, Object> entry : flash.entrySet()) {
      request.setAttribute(entry.getKey(), entry.getValue());
    }
  }

  protected void loadFlash(HttpServletRequest request) {
    Map<String, Object> flashMap = getAndRemoveSessionFlash(request);

    if (flashMap != null) {
      setRequestAttributes(flashMap, request);
    }
  }

  protected void storeFlash(Map<String, Object> flash, HttpServletRequest request) {
    if (!flash.isEmpty()) {
      request.getSession().setAttribute(FLASH_SESSION_ATTR, flash);
    }
  }

  private HttpServletRequest request() {
    return httpServletRequestProvider.get();
  }

  private Flash flash() {
    return flashProvider.get();
  }
}
