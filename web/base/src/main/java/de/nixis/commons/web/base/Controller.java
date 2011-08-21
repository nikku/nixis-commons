/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base;

import de.nixis.commons.jersey.mvc.MVCView;
import de.nixis.commons.web.base.flash.FlashMap;
import de.nixis.commons.web.base.flash.FlashMapHelper;
import de.nixis.commons.web.base.model.UserBase;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;

/**
 * Provides basic controller functionality.
 *
 * @author markus.goetz
 * @author nico.rehwaldt
 */
public abstract class Controller {

    @Context
    private SecurityContext securityContext;

    @Context
    private HttpServletRequest request;
    
    /**
     * Return a response builder to build a redirect response
     * @param path to redirect the response to
     * @return
     */
    protected Response redirect(String path) {
        URI redirectURL = UriBuilder.fromPath(path).build();
        return Response.seeOther(redirectURL).build();
    }

    /**
     * Return the currently logged on user if any
     * 
     * @return
     */
    protected UserBase getActiveUser() {
        return (UserBase) securityContext.getUserPrincipal();
    }

    /**
     * Render the specified template
     * 
     * @param path path to template inside model folder
     * @param model model object to pass to template
     * @return template view
     */
    protected MVCView render(String path, Object model) {
        return MVCView.template(path, model);
    }

    /**
     * Render the specified template without a model instance
     *
     * @param path
     * @return
     */
    protected MVCView render(String path) {
        return render(path, null);
    }

    /**
     * Adds a message to flash
     * @param message
     * @param args
     */
    protected void addFlashMessage(String message, Object ... args) {
        addToFlashContainer(message, "messages", args);
    }

    private void addToFlashContainer(String message, String containerName, Object ... args) {
        List<String> container = (List<String>) flash().get(containerName);
        if (container == null) {
            container = new ArrayList<String>();
            flash().put(containerName, container);
        }

        container.add(String.format(message, args));
    }

    /**
     * Adds a error to flash
     * @param error
     * @param args
     */
    protected void addFlashError(String message, Object ... args) {
        addToFlashContainer(message, "errors", args);
    }

    /**
     * Get flash context
     * @return
     */
    protected FlashMap flash() {
        return FlashMapHelper.getFlashMap(request);
    }
}
