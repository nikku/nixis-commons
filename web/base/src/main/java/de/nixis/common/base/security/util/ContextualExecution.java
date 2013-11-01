package de.nixis.common.base.security.util;

import de.nixis.common.base.security.SecurityContextHolder;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author nico.rehwaldt
 */
public abstract class ContextualExecution implements Runnable {

  /**
   * Runs this contextual execution using the specified security context
   *
   * @param context
   */
  public void runWith(SecurityContext context) {
    SecurityContextHolder.runInContext(this, context);
  }
}