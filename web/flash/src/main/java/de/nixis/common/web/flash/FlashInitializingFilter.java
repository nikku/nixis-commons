package de.nixis.common.web.flash;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.collection.Ref;

/**
 * Filter which initializes the flash context in an application
 *
 * @author nico.rehwaldt
 */
@Provider
@Singleton
@Priority(0)
public class FlashInitializingFilter implements ContainerRequestFilter {

  @Inject
  private javax.inject.Provider<Ref<Flash>> flashFactory;

  @Override
  public void filter(ContainerRequestContext context) throws IOException {
    SimpleFlash flash = new SimpleFlash();
    flashFactory.get().set(flash);
  }
}
