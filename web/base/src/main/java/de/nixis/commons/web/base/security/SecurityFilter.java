/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base.security;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import de.nixis.commons.web.base.model.UserBase;
import de.nixis.commons.web.base.flash.FlashMapHelper;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author nico.rehwaldt
 * @author markus.goetz
 */
@Component
public class SecurityFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(SecurityFilter.class.getName());

    @Autowired
    private UserLoginManager userLoginManager;
    
    @javax.ws.rs.core.Context
    private HttpServletRequest hsr;

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        UserBase user = (UserBase) hsr.getSession().getAttribute("user");
        if (user == null) {
            Cookie cookie = request.getCookies().get("uid");
            if (cookie != null) {
                user = userLoginManager.loginViaAuthToken(cookie.getValue());

                if (user != null) {
                    hsr.getSession().setAttribute("currentUser", user);
                }
            }
        }
        
        // Initialize flash map
        FlashMapHelper.initFlashMap(hsr);

        // Set request's security context
        request.setSecurityContext(new Context(user));
        return request;
    }
    
    private class Context implements SecurityContext {

        private UserBase user;
        
        public Context(UserBase user) {
            this.user = user;
        }

        @Override
        public UserBase getUserPrincipal() {
            return user;
        }

        @Override
        public boolean isUserInRole(String role) {
            if ("user".equals(role)) {
                return user != null;
            } else if ("admin".equals(role)) {
                return user != null && user.isAdmin();
            }
            
            return false;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public String getAuthenticationScheme() {
            return FORM_AUTH;
        }
    }
}
