package de.nixis.common.base.web;

import de.nixis.common.base.model.UserBase;
import de.nixis.common.base.security.SecurityContextHolder;
import de.nixis.common.base.security.util.ContextualExecution;
import de.nixis.common.base.security.util.SimpleSecurityContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nico.rehwaldt
 */
public class ControllerTest {

  /**
   * Test of getActiveUser method, of class Controller.
   */
  @Test
  public void testGetActiveUserWithNoUser() {
    final ControllerImpl testController = new ControllerImpl();

    new ContextualExecution() {
      @Override
      public void run() {
        assertNull(testController.getActiveUser());
      }
    }.runWith(SecurityContextHolder.NULL_CONTEXT);

    new ContextualExecution() {
      @Override
      public void run() {
        assertNull(testController.getActiveUser());
      }
    }.runWith(null);
  }

  @Test
  public void testActiveUserWithUser() {
    final ControllerImpl testController = new ControllerImpl();

    final UserBase KLAUS = new UserBase("Klaus", "klaus@bar") {
      @Override
      public boolean hasRole(String role) {
        return false;
      }
    };

    new ContextualExecution() {
      @Override
      public void run() {
        assertEquals(KLAUS, testController.getActiveUser());
      }
    }.runWith(new SimpleSecurityContext(KLAUS));
  }

  private static class ControllerImpl extends Controller {

    public Object foo() {
      return new Object();
    }
  }
}
