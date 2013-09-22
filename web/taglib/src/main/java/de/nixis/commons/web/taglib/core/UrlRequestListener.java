package de.nixis.commons.web.taglib.core;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Examines an incomming request and stores a path separator in the request
 * attributes which is based on the request uri.
 *
 * @author nico.rehwaldt
 */
public class UrlRequestListener implements ServletRequestListener {

  @Override
  public void requestDestroyed(ServletRequestEvent sre) {
  }

  @Override
  public void requestInitialized(ServletRequestEvent sre) {
    ServletRequest sr = sre.getServletRequest();

    if (sr instanceof HttpServletRequest) {
      HttpServletRequest r = (HttpServletRequest) sr;

      r.setAttribute(
          TagUtil.ORIGINAL_REQUEST_URI_ATTR,
          r.getRequestURI());
    }
  }
}
