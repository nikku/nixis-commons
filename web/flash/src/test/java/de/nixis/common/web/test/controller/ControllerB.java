package de.nixis.common.web.test.controller;

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
@Path("b")
public class ControllerB {

  @GET
  public String get(@Context Flash flash) {
    if (flash == null) {
      throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
    }
    
    return "SUCCESS";
  }
}
