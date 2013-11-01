package de.nixis.common.base.security;

import de.nixis.common.base.security.util.SimpleSecurityContext;
import javax.ws.rs.core.SecurityContext;

/**
 * Holder of the current security context
 *
 * @author nico.rehwaldt
 */
public class SecurityContextHolder {

  /**
   * A context without a user
   */
  public static final SecurityContext NULL_CONTEXT = new SimpleSecurityContext();

  private static ThreadLocal<SecurityContext> threadLocal = new ThreadLocal<>();

  /**
   * Set the security context for the current thread
   *
   * @return
   */
  public static SecurityContext getContext() {
    return threadLocal.get();
  }

  /**
   * Return the security context for the current thread
   *
   * @param context
   */
  public static void setContext(SecurityContext context) {
    threadLocal.set(context);
  }

  /**
   * Run the specified runnable in the given security context
   *
   * @param context
   * @param runnable
   */
  public static void runInContext(Runnable runnable, SecurityContext context) {
    SecurityContext old = threadLocal.get();
    threadLocal.set(context);
    runnable.run();
    threadLocal.set(old);
  }
}
