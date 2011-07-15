package de.nixis.commons.web.filter;

import de.nixis.commons.web.ErrorRequestInfoWrapper;
import de.nixis.commons.web.Flash;
import de.nixis.commons.web.GenericClass;
import de.nixis.commons.web.RequestInfoWrapper;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dummdoedel
 */
public class ErrorRequestWrapperFilter extends GenericClass implements Filter {
    
    private FilterConfig filterConfig = null;
    
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException { 
        
        ErrorRequestInfoWrapper wrapper;
        
        if (request instanceof ErrorRequestInfoWrapper) {
            wrapper = (ErrorRequestInfoWrapper) request;
        } else {
            Flash flash = (Flash) ((HttpServletRequest)request).getSession().getAttribute("__Flash");
            RequestInfoWrapper oldWrapper = (RequestInfoWrapper) flash.getAttribute("__RequestInfoWrapper");

            // NullPointerException here (l. 47)... occasionally at app deployment - ignore
            try {
                wrapper = new ErrorRequestInfoWrapper((HttpServletRequest) request, oldWrapper.getPathLevelJumper());
                getLogger().info("['" + wrapper.getRealRequestURI() + "'] wrapped request with error wrapper");
                wrapper.logRequestInfo();
            } catch (NullPointerException npe) {
                /* @todo gute frage. ich denke mal einfach ignorieren (tritt nur direkt nach dem server start auf) */
                npe.printStackTrace();
                return;
            }
        }
        
        chain.doFilter(wrapper, response);
    }
    
    /**
     * Destroy method for this filter 
     */
    @Override
    public void destroy() {
        getLogger().info("Destroyed");
    }
    
    /**
     * Init method for this filter 
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        getLogger().info("Initialized");
    }
}