/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base;

import de.nixis.commons.jersey.mvc.MVCView;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author nico.rehwaldt
 */
@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {

    @Context
    private HttpServletRequest request;
    
    @Override
    public Response toResponse(Throwable exception) {
        Response r = toResponseInternal(exception);
        return r;
    }

    private Response toResponseInternal(WebApplicationException exception) {
        MVCView view = MVCView.template("error/error", exception);

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