/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nixis.commons.web.base.security.util;

import de.nixis.commons.web.base.security.SecurityContextHolder;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author nico.rehwaldt
 */
public abstract class ContextualExecution implements Runnable {
    
    /**
     * Runs this contextual execution using the specified security context
     * @param context 
     */
    public void runWith(SecurityContext context) {
        SecurityContextHolder.runInContext(this, context);
    }
}