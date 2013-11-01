/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nixis.common.base.security.util;

import de.nixis.common.base.security.util.SimpleSecurityContext;
import de.nixis.common.base.security.util.ContextualExecution;
import de.nixis.common.base.model.UserBase;
import de.nixis.common.base.security.SecurityContextHolder;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.SecurityContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author nico
 */
public class ContextualExecutionTest {
    
    public ContextualExecutionTest() {
    }
    private static UserBase KLAUS = new UserBase("Klaus", "klaus@bar") {
        @Override
        public boolean hasRole(String role) {
            return false;
        }
    };

    private static UserBase NULL = null;

    private static UserBase ERWIN = new UserBase("Erwin", "erwin@bar") {
        @Override
        public boolean hasRole(String role) {
            return false;
        }
    };

    /**
     * Test of runWith method, of class ContextualExecution.
     */
    @Test
    public void testRunInContext() {
        SecurityContext context = new SimpleSecurityContext(KLAUS);
        
        new ContextualExecution() {
            @Override
            public void run() {
                assertEquals(KLAUS.getName(), 
                        SecurityContextHolder.getContext().getUserPrincipal().getName());
            }
        }.runWith(context);
    }
    
    /**
     * Test of runWith method, of class ContextualExecution.
     */
    @Test
    public void testRunInContext2() {
        new ContextualExecution() {
            @Override
            public void run() {
                assertEquals(
                    KLAUS.getName(), 
                    SecurityContextHolder.getContext().getUserPrincipal().getName());
            }
        }.runWith(new SimpleSecurityContext(KLAUS));
        
        new ContextualExecution() {
            @Override
            public void run() {
                assertEquals(
                    ERWIN.getName(), 
                    SecurityContextHolder.getContext().getUserPrincipal().getName());
            }
        }.runWith(new SimpleSecurityContext(ERWIN));
    }
}
