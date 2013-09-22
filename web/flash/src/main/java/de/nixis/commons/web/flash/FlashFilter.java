package de.nixis.commons.web.flash;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import javax.ws.rs.core.Context;

/**
 * Filter which initializes the flash context in an application
 *
 * @author nico.rehwaldt
 */
public class FlashFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final String FLASH_SESSION_ATTR = FlashFilter.class.getName() + "__FLASH";

  @Context
  private HttpServletRequest httpRequest;

  @Override
  public void filter(ContainerRequestContext context) throws IOException {
    loadFlashFromSession(httpRequest);

    FlashHolder.set(new HashMap<String, Object>());
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    Map<String, Object> flash = FlashHolder.get();

    setSessionFlash(flash, httpRequest);

    FlashHolder.set(null);
  }

  protected Map<String, Object> getAndRemoveSessionFlash(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
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

  protected void loadFlashFromSession(HttpServletRequest request) {
    Map<String, Object> flash = getAndRemoveSessionFlash(request);

    if (flash != null) {
      setRequestAttributes(flash, request);
    }
  }

  protected void setSessionFlash(Map<String, Object> flash, HttpServletRequest request) {
    if (!flash.isEmpty()) {
      request.getSession().setAttribute(FLASH_SESSION_ATTR, flash);
    }
  }
}
