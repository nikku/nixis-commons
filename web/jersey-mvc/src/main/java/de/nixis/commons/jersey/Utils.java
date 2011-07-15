/*
 * Part of nixis.commons
 */
package de.nixis.commons.jersey;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides some commonly used utility methods / constants
 * @author nico
 */
public class Utils {

    /**
     * Constant which can be passed to a {@link javax.ws.rs.Produces} annotation
     * to indicate that html is more likely produced than xml or json.
     *
     * This should be the default when creating controller behaviour which
     * should serve both XML/JSON (for testing) and HTML in production.
     */
    public static final String PRODUCES_HTML_IN_FAVOUR_OF_XML =
        "text/html;qs=4, application/xhtml+xml;qs=3, application/json;qs=2, application/xml;qs=1";

    /**
     * Returns true if the given request is ajax based on the requests header.
     * 
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
