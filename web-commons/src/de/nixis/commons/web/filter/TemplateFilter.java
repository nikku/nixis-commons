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

/**
 *
 * @author Muffelbox
 */
public class TemplateFilter extends GenericClass implements Filter {
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
        RequestInfoWrapper wrapper = (RequestInfoWrapper) request;
        
        getLogger().info("['" + wrapper.getRealRequestURI() + "'] Processing request");
        
        try {
            // next filter in chain
            chain.doFilter(request, response);

            // process template
            wrapper.processTemplate(response);
        } catch(Throwable t) {
            getLogger().error("Processing of request failed due to exception", t);
        }
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


