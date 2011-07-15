package de.nixis.commons.web.filter;

import de.nixis.commons.web.GenericClass;
import de.nixis.commons.web.RequestInfoWrapper;

import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Muffelbox
 */
public class SecurityFilter extends GenericClass implements Filter {
    
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
        chain.doFilter(wrapper, response);
        
//        // Obtain visitor session and required privileges
//        List<Privilege> requiredPrivileges = wrapper.getRequiredPrivileges();
//        VisitorSession session = wrapper.getVisitorSession();
//
//        getLogger().info("['" + wrapper.getRealRequestURI() + "'] Filtering request");
//        getLogger().info("['" + wrapper.getRealRequestURI() + "'] Privileges required: " + requiredPrivileges.toString());
//
//        // Query the session whether it holds all required privileges
//        PrivilegeStatus status = session.hasRequiredPrivileges(requiredPrivileges);
//
//        switch (status) {
//            case NO_PRIVILEGES_NEEDED:
//            case OK:
//                getLogger().info("['" + wrapper.getRealRequestURI() + "'] The provided privileges are sufficient, access granted");
//                chain.doFilter(wrapper, response);
//                break;
//            default:
//                getLogger().info("['" + wrapper.getRealRequestURI() + "'] The provided privileges are not sufficient, access denied");
//                ((HttpServletResponse)response).sendError(HttpServletResponse.SC_FORBIDDEN);
//        }
    }

    /**
     * Destroy method for this filter 
     */
    @Override
    public void destroy() {
        getLogger().info("destroyed");
    }

    /**
     * Init method for this filter 
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {        
	this.filterConfig = filterConfig;        
        getLogger().info("initialized");
    }
}