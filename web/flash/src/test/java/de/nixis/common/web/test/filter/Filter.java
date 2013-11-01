package de.nixis.common.web.test.filter;

import java.io.IOException;

import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import de.nixis.common.web.flash.Flash;

/**
 *
 * @author nico.rehwaldt
 */
@Provider
@Singleton
public class Filter implements ContainerRequestFilter {

  @Context
  private javax.inject.Provider<Flash> flash;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    if (flash.get() == null) {
      requestContext.abortWith(Response.serverError().build());
    }
  }
}