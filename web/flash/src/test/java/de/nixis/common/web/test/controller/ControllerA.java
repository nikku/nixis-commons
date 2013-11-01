package de.nixis.common.web.test.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

import de.nixis.common.web.flash.Flash;

/**
 *
 * @author nico.rehwaldt
 */
@Path("a")
@RequestScoped
public class ControllerA {

  @Inject
  private Provider<Flash> flashProvider;

  @GET
  public String get() {
    if (flashProvider.get() == null) {
      throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
    }

    return "SUCCESS";
  }
}
