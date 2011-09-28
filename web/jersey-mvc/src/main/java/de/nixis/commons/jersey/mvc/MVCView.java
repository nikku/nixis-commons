/*
 * Part of nixis.commons
 */
package de.nixis.commons.jersey.mvc;

import com.sun.jersey.api.container.ContainerException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.core.Response;

/**
 * This class holds information about the view to use and the model class
 * to render.
 *
 * It provides some utility methods which can be used in controllers, as well.
 *
 * @see MVCView#redirect(java.lang.String)
 * @see MVCView#template(java.lang.String, java.lang.Object)
 * 
 * @author nico.rehwaldt
 */
public class MVCView {

    public static final String MVC_VIEW_FORWARD = "MVC_VIEW_FORWARD";

    private final String template;
    private final Object model;
    
    private MVCView(String template, Object model) {
        this.template = template;
        this.model = model;
    }

    /**
     * Return the template passed to this view instance
     * 
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Return the model passed to this view instance
     * 
     * @return the model provided within the view
     */
    public Object getModel() {
        return model;
    }

    /**
     * Under a given base path, return the path for the view.
     *
     * @param basePath to be used for view computation
     *
     * @return path for view under a given base path
     */
    public String getPath(String basePath) {
        StringBuilder builder = new StringBuilder(basePath);
        if (!template.startsWith("/")) {
            builder.append("/");
        }
        builder.append(template).append(".jsp");
        return builder.toString();
    }
    
    /**
     * Returns a new MVCView which indicates that the given template should be
     * rendered with the given model.
     *
     * This method is meant to be statically included in controller classes.
     * 
     * @param template to be rendered
     * @param model to be used for rendering
     *
     * @return mvc view holding the information
     */
    public static MVCView template(String template, Object model) {
        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }
        
        return new MVCView(template, model);
    }

    /**
     * Returns a response indicating that a redirect should be
     * started to the given uri. If the uri is relative, it will
     * be interpreted relative to the applications base directory.
     *
     * This method is meant to be statically included in controller classes.
     * 
     * @param uri to redirect to
     * 
     * @return configured response
     */
    public static Response redirect(String uri) {
        try {
            return Response.seeOther(new URI(uri)).build();
        } catch (URISyntaxException e) {
            throw new ContainerException(e);
        }
    }
}
