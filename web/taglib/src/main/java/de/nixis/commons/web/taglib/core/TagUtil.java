/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.nixis.commons.web.taglib.core;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utilty class which provides methods for tag libs
 * @author nico.rehwaldt
 */
public class TagUtil {

    public static final String ORIGINAL_REQUEST_URI = "de.nixis.commons.web.taglib.url.ORIGINAL_REQUEST_URI";
    
    public static String rewriteURL(
            HttpServletRequest request,
            HttpServletResponse response,
            String path) {

        String url;
        
        if (isAbsolute(path)) {
            String requestUri = null;

            // Try to extract request uri from referrer instead of current 
            // request uri if the request is ajax (the page scope is the
            // one of the original page
            if (isAjax(request)) {
                String referer = request.getHeader("Referer");
                if (referer != null) {
                    try {
                        URI uri = new URI(referer);
                        requestUri = uri.getPath();
                    } catch (URISyntaxException e) { ; }
                }
            } else {
                requestUri = (String) request.getAttribute(ORIGINAL_REQUEST_URI);
            }

            if (requestUri != null) {
                String pathSeparator =
                    getPathSeparator(request.getContextPath(), requestUri);
                    
                url = pathSeparator + path;
            } else {
                // Fall back solution in case request uri could not be obtained
                // this will break applications which do not live under
                // the specified context path
                url = request.getContextPath() + path;
            }
        } else {
            url = path;
        }

        return response.encodeURL(url);
    }

    private static boolean isAbsolute(String path) {
        return path.charAt(0) == '/';
    }
    
    private static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public static String getPathSeparator(String contextPath, String url) {
        String uri = url.replaceFirst(contextPath, "");

        StringBuilder buffer = new StringBuilder();

        for (int i = 1; i < uri.length(); i++) {
            if (uri.charAt(i) == '/') {
                if (buffer.length() > 0) {
                    buffer.append("/");
                }
                buffer.append("..");
            }
        }

        String pathSeparator = ".";
        if (buffer.length() > 0) {
            pathSeparator = buffer.toString();
        }

        return pathSeparator;
    }

    /**
     * Returns true, if o is an instance of the specified class
     * @param o
     * @param cls
     * @return
     */
    public static boolean instanceOf(Object o, String className) {
        try {
            return Class.forName(className).isInstance(o);
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
}
