package de.nixis.commons.web.filter;

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
public class RequestWrapperFilter extends GenericClass implements Filter {
    
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
        
        RequestInfoWrapper wrapper;
        
        if (request instanceof RequestInfoWrapper) {
            wrapper = (RequestInfoWrapper) request;
        } else {
            wrapper = new RequestInfoWrapper((HttpServletRequest) request);
            getLogger().info("['" + wrapper.getRealRequestURI() + "'] wrapped request");
            wrapper.logRequestInfo();
        }
        
        chain.doFilter(wrapper, response);
    }

    /**
     * Destroy method for this filter 
     */
    @Override
    public void destroy() { }

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