/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nixis.commons.web.base.security;

import de.nixis.commons.web.base.security.util.SimpleSecurityContext;
import javax.ws.rs.core.SecurityContext;

/**
 * Holder of the current security context
 * @author nico.rehwaldt
 */
public class SecurityContextHolder {
    
    /**
     * A context without a user
     */
    public static final SecurityContext NULL_CONTEXT = 
            new SimpleSecurityContext();
    
    private static ThreadLocal<SecurityContext> contextThreadLocal = 
            new ThreadLocal<SecurityContext>();
    
    /**
     * Set the security context for the current thread
     * @return 
     */
    public static SecurityContext getContext() {
        return contextThreadLocal.get();
    }
    
    /**
     * Return the security context for the current thread
     * @param context 
     */
    public static void setContext(SecurityContext context) {
        contextThreadLocal.set(context);
    }
    
    /**
     * Run the specified runnable in the given security context
     * 
     * @param context
     * @param runnable 
     */
    public static void runInContext(Runnable runnable, SecurityContext context) {
        SecurityContext old = contextThreadLocal.get();
        contextThreadLocal.set(context);
        runnable.run();
        contextThreadLocal.set(old);
    }
}
