/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base;

import javax.persistence.NoResultException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.jersey.server.mvc.Viewable;

/**
 *
 * @author nico.rehwaldt
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    Response r = toResponseInternal(exception);
    return r;
  }

  private Response toResponseInternal(WebApplicationException exception) {
    Viewable view = new Viewable("/error/error", exception);

    return Response.fromResponse(exception.getResponse())
        .status(Response.Status.OK)
        .type(MediaType.TEXT_HTML_TYPE)
        .entity(view)
        .build();
  }

  private Response toResponseInternal(Throwable exception) {
    if (exception instanceof WebApplicationException) {
      return toResponseInternal((WebApplicationException) exception);
    } else if (exception instanceof NoResultException) {
      return toResponseInternal(new WebApplicationException(exception, Response.Status.NOT_FOUND));
    } else {
      return toResponseInternal(new WebApplicationException(exception));
    }
  }
}