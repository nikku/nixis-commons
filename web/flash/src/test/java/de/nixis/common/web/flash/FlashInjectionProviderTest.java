package de.nixis.common.web.flash;

import javax.ws.rs.core.Response;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import de.nixis.common.web.test.TestApplication;

import static org.fest.assertions.Assertions.assertThat;

import javax.ws.rs.core.Response.Status.Family;

/**
 *
 * @author nico.rehwaldt
 */
public class FlashInjectionProviderTest extends JerseyTest {

  public FlashInjectionProviderTest() {
    super(TestApplication.class);
  }

  @Test
  public void shouldInjectFlashOnInstance() {
    Response response = target("/a").request().get();

    assertThat(response.getStatusInfo().getFamily()).isEqualTo(Family.SUCCESSFUL);
  }

  @Test
  public void shouldInjectFlashOnMethod() {
    Response response = target("/b").request().get();

    assertThat(response.getStatusInfo().getFamily()).isEqualTo(Family.SUCCESSFUL);
  }
}
