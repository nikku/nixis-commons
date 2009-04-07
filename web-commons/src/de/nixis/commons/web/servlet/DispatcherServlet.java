package de.nixis.commons.web.servlet;

import de.nixis.commons.web.GenericServlet;
import de.nixis.commons.web.RequestInfoWrapper;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

/**
 * This servlet acts as a dispatcher to simple POJOs which are the Controllers.
 * It forwards a request to <s>/boo/far</s> to POJO <b>BooController</b> in
 * the package set via DispatcherServlet.setControllerBasePackage. The method
 * executed on the controller is <s>far</s>.
 *
 * DispatcherServlet.setControllerBasePackage has to be invoked before the first
 * invokation of the dispatcher servlet.
 * 
 * @author Dummdoedel
 */
public class DispatcherServlet extends GenericServlet {
    
    /**
     * Invoke controller associated with current request.
     * Does not care about the HTTP method.
     * 
     * @param request
     * @param response
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        RequestInfoWrapper wrapper = (RequestInfoWrapper) request;

        String uriParts[] = wrapper.getRealRequestURI().split("/");
        if (uriParts.length < 2) throw new IllegalArgumentException("No controller / action can be extracted from request uri");
        
        System.out.println(Arrays.toString(uriParts));

        String controller = StringUtils.capitalize(uriParts[uriParts.length - 2]);
        String action = uriParts[uriParts.length - 1];

        String path = StringUtils.join(Arrays.copyOf(uriParts, uriParts.length - 2), ".").toLowerCase();
        
        System.out.println(controller);
        System.out.println(action);
        System.out.println(basePath + "." + path);
        
        //Class.forName(basePath + "." + 2);
    }

    private static String basePath;

    /**
     * Sets the controller base package to use to search for controllers
     * @param basePath
     */
    public static void setControllerBasePackage(String basePath) {
        DispatcherServlet.basePath = basePath;
    }
}
