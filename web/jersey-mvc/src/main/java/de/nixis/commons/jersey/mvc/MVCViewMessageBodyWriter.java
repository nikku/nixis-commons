/*
 * Part of knowledge engineering (ke) course work, 2010/11
 */
package de.nixis.commons.jersey.mvc;

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.server.impl.container.servlet.JSPTemplateProcessor;
import com.sun.jersey.server.impl.container.servlet.RequestDispatcherWrapper;
import com.sun.jersey.server.impl.template.ViewableMessageBodyWriter;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * MessageBodyWriter which supports views in a manner which is suitable for real
 * MVC. That is, the view selection is not based on the returning entity but
 * on the resource class (<em>controller</em>) and resource method
 * (<em>action</em>) used to process the request.
 *
 * The general rule is, that views have to be found in
 * +basePath+/+controller+/+action+.jsp, whereas controller and action are
 * converted to lower case.
 * BasePath can be customized using the
 * {@link ServletContainer.JSP_TEMPLATES_BASE_PATH} configuration parameter.
 *
 * This implementation adapted {@link ViewableMessageBodyWriter} and
 * {@link JSPTemplateProcessor}.
 * 
 * @author nico.rehwaldt
 */
@Provider
@Produces("text/html")
public class MVCViewMessageBodyWriter implements MessageBodyWriter<Object> {

    private static final Logger LOGGER =
        Logger.getLogger(MVCViewMessageBodyWriter.class.getName());

    private static final Set<Class<?>> NO_MODEL_CLASSES;

    static {
        NO_MODEL_CLASSES = new HashSet<Class<?>>();
        NO_MODEL_CLASSES.add(Response.class);
        NO_MODEL_CLASSES.add(Viewable.class);
    }

    @Context
    private HttpContext context;

    @Context
    private ServletContext servletContext;

    @Context
    private ThreadLocal<HttpServletRequest> requestInvoker;

    @Context
    private ThreadLocal<HttpServletResponse> responseInvoker;

    private final String basePath;

    public MVCViewMessageBodyWriter(
            @Context ResourceConfig resourceConfig) {

        String path = (String) resourceConfig.getProperties().get(
                ServletContainer.JSP_TEMPLATES_BASE_PATH);
        if (path == null) {
            this.basePath = "";
        } else if (path.charAt(0) == '/') {
            this.basePath = path;
        } else {
            this.basePath = "/" + path;
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {

        Object model = context.getResponse().getEntity();

        // If class is not a model class
        // (on the ignore list)
        if (NO_MODEL_CLASSES.contains(model.getClass())) {
            LOGGER.log(Level.WARNING, "[MVC] Model class {0} is ignored", type);
            return false;
        }

        // MVCViews are special cases, which can be used to render templates,
        // which do not equal the "standard" ones. If a special template should be
        // rendered which does not exist, this is treated as a serious programming
        // error.
        if (model instanceof MVCView) {
            MVCView view = (MVCView) model;
            if (!templateExists(view)) {
                String errorMessage = String.format(
                    "Template '%s' specified in MVCView does not exist",
                    view.getTemplate());

                LOGGER.severe(errorMessage);
                throw new ContainerException(errorMessage);
            }
            return true;
        }

        // The check for whether an entity is writable or not does not depend
        // on the entity itself, instead it is checked, if a jsp template
        // exists on the path [baseurl]/[controller]/[action].jsp.
        // If the view url exists, the object (which ever it is) can be written.
        return templateExists(getViewPath(context));
    }

    @Override
    public void writeTo(Object o,
            Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
            OutputStream out) throws IOException {

        // Path to template
        String path = null;
        Object model = o;

        // If object ist MVCView we will extract the path from the source and
        // we still have to check if the template instance exists.
        if (o instanceof MVCView) {
            MVCView view = (MVCView) o;
            model = view.getModel();
            path = view.getPath(basePath);
        } else {
            // As we already checked in #isWritable, whether the view exists,
            // we will only forward the action here
            path = getViewPath(context);
        }

        RequestDispatcher d = servletContext.getRequestDispatcher(path);
        if (d == null) {
            throw new ContainerException(
                "No request dispatcher for found for " + path);
        }

        // Commit the status and headers to the HttpServletResponse
        out.flush();

        d = new RequestDispatcherWrapper(d, basePath, context, new Viewable(path, model));

        try {
            HttpServletRequest request = requestInvoker.get();
            request.setAttribute(MVCView.MVC_VIEW_FORWARD, MVCView.MVC_VIEW_FORWARD);

            // Forward using normal http servlet technology
            d.forward(request, new GlassfishErrorPageRenderFix(responseInvoker.get()));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to forward to view", e);
            throw new ContainerException("Failed to forward to view");
        }
     }

    @Override
    public long getSize(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // Return -1 to indicate that no explicit content lenght can be assigned
        return -1;
    }

    /**
     * Return the view path from the http context.
     *
     * The path soley depends on the method which was used to process the
     * request, if the request was processed by +Foo#bar()+ then the
     * controller will be set to +foo+ and the action will be set to +bar+.
     *
     * Controller names will be converted to lower case, whereas actions
     * remain as they are (e.g. respecting camel case).
     *
     * @param context the context to extract the data from
     *
     * @return path to view
     */
    private String getViewPath(HttpContext context) {
        AbstractResourceMethod m = context.getUriInfo().getMatchedMethod();

        String controller = m.getMethod().getDeclaringClass().getSimpleName();
        String action = m.getMethod().getName();

        if (controller.endsWith("Controller")) {
            controller = controller.substring(0, controller.length() - "Controller".length());
        }

        controller = controller.toLowerCase();

        String path = basePath + "/" + controller + "/" + action + ".jsp";
        return path;
    }

    /**
     * Checks if the template exists under a given base path in the specified
     * servlet context
     *
     * @param view mvc view
     * @return
     */
    private boolean templateExists(MVCView view) {
        return templateExists(view.getPath(basePath));
    }

    /**
     * Return true if the given path exists as a template
     * @param path
     * @return
     */
    public boolean templateExists(String path) {
        try {
            if (!path.endsWith(".jsp")) {
                path = path + ".jsp";
            }
            return servletContext.getResource(path) != null;
        } catch (MalformedURLException e) {
            // TODO: log
            return false;
        }
    }

    public static class IgnorantOutputStream extends ServletOutputStream {

        private boolean ignore = false;
        private final ServletOutputStream out;
        
        public IgnorantOutputStream(ServletOutputStream out) {
            this.out = out;
        }

        public void ignore() {
            this.ignore = true;
        }

        @Override
        public void write(int b) throws IOException {
            if (!ignore) {
                out.write(b);
            }
        }
    }

    public static class GlassfishErrorPageRenderFix extends HttpServletResponseWrapper {

        private final IgnorantOutputStream out;

        public GlassfishErrorPageRenderFix(HttpServletResponse response) throws IOException {
            super(response);
            
            this.out = new IgnorantOutputStream(response.getOutputStream());
        }

        @Override
        public IgnorantOutputStream getOutputStream() throws IOException {
            return out;
        }
    }
}
