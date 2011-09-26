/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nixis.commons.web.flash;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter which initializes the flash context in an application
 * @author nico.rehwaldt
 */
public class FlashFilter implements Filter {
    
    private static final String ONLY_ONCE_PREFIX = "__FilterExecuted_";

    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void doFilter(
            ServletRequest request, 
            ServletResponse response, 
            FilterChain chain) throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest && 
            response instanceof HttpServletResponse) {
            
            doFilterInternal(
                    (HttpServletRequest) request, 
                    (HttpServletResponse) response);
            
            chain.doFilter(request, response);
        } else {
            throw new ServletException("Not in a http container");
        }
    }

    /**
     * Initialize the flash *once* per request.
     * 
     * @param request
     * @param response 
     */
    private void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response) {
        
        if (getOnlyOnceFlag(request) != null) {
            return;
        }
        
        FlashHelper.initFlash(request);
        setOnlyOnceFlag(request);
    }
    
    @Override
    public void destroy() {}
    
    private Object getOnlyOnceFlag(HttpServletRequest request) {
        return request.getAttribute(ONLY_ONCE_PREFIX + getClass().getName());
    }
    
    private void setOnlyOnceFlag(HttpServletRequest request) {
        request.setAttribute(ONLY_ONCE_PREFIX + getClass().getName(), "TRUE");
    }
}
