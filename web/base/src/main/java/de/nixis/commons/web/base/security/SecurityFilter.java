/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base.security;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import de.nixis.commons.web.base.model.UserBase;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.SecurityContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author nico.rehwaldt
 * @author markus.goetz
 */
@Component
public class SecurityFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(SecurityFilter.class.getName());

    @Inject
    private UserLoginManager userLoginManager;
    
    @Context
    private ThreadLocal<HttpServletRequest> tlr;

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        HttpServletRequest r = tlr.get();
        if (r == null) {
            LOGGER.severe("Tried to execute filter outside context of a request");
            
            throw new IllegalStateException(
                "Can only be used in the context of a request");
        }
        
        UserBase user = (UserBase) r.getSession().getAttribute("currentUser");
        if (user == null) {
            Cookie cookie = request.getCookies().get("uid");
            if (cookie != null) {
                user = userLoginManager.loginViaAuthToken(cookie.getValue());

                if (user != null) {
                    r.getSession().setAttribute("currentUser", user);
                    LOGGER.fine("Logged in current user via session");
                }
            }
        }
        
        // Set request's security context
        request.setSecurityContext(new SimpleSecurityContext(user));
        return request;
    }
}
