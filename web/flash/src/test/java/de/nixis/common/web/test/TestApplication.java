package de.nixis.common.web.test;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

import de.nixis.common.web.flash.FlashBinder;
import de.nixis.common.web.flash.FlashInitializingFilter;

/**
 *
 * @author nico.rehwaldt
 */
@ApplicationPath("")
public class TestApplication extends ResourceConfig {

  public TestApplication() {
    packages(TestApplication.class.getPackage().getName());

    register(FlashInitializingFilter.class);
    register(new FlashBinder());
  }
}
