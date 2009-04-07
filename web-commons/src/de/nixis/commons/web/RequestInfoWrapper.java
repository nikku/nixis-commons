package de.nixis.commons.web;

import de.nixis.commons.ejb.bean.PageMappingManager;
import de.nixis.commons.ejb.domain.PageMapping;
import de.nixis.commons.ejb.domain.Privilege;
import de.nixis.commons.web.ParameterNotFoundException;
import de.nixis.configuration.bean.Configuration;
import de.nixis.logging.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * This class wraps the request and adds additional functionality to it
 * @author Dummdoedel
 */
public class RequestInfoWrapper extends HttpServletRequestWrapper {

    /**
     * Status indicating that this request has a rewritten uri
     */
    protected static final int HAS_REWRITTEN_URI = 2;
    /**
     * Status indicating that this request was already parsed
     */
    protected static final int IS_PARSED = 4;
    /**
     * Status indicating, that this request has an extension, 
     * which is ignored
     */
    protected static final int HAS_IGNORED_EXTENSION = 8;
    /**
     * Status indicating, that this request has been filtered
     * by the template filter
     */
    protected static final int IS_TEMPLATE_FILTERED = 16;
    /**
     * Status indicating that this request has been dispatched
     * by the proxy filter
     */
    protected static final int IS_DISPATCHED = 32;

    /**
     * Status indicating that this request has been logged.
     */
    protected static final int IS_LOGGED = 64;
    
    private Configuration config;
    private PageMappingManager pageMappingManager;

    private Logger logger;
    
//    private VisitorSession visitorSession;

    private int status = 0;
    
    private String pathLevelJumper = "";
    private String realRequestURI = null;

    /**
     * The part of the original request uri which caused the real request uri
     * to "match"
     */
    private String mappingURIPart = null;

    private String[] uriParameters = new String[0];
    
    private List<Privilege> requiredPrivileges = new ArrayList<Privilege>();
    
    private Flash flash;
    
    /**
     * Constructs a new Wrapper instance
     * @param request
     */
    public RequestInfoWrapper(HttpServletRequest request) {
        super(request);
        
        logger = InitialApplicationContext.getLogger(getClass());
        config = InitialApplicationContext.getConfig();

        pageMappingManager = InitialApplicationContext.lookupBean(PageMappingManager.class);
        
        flash = (Flash) getSession().getAttribute("__Flash");
//        visitorSession = (VisitorSession) getSession().getAttribute("__VisitorSession");
        
        if (!flash.containsAttribute("__RequestInfoWrapper")) {
            flash.setTemporaryAttribute("__RequestInfoWrapper", this);
        }
    }

    /**
     * Answer, whether this request has already been parsed
     * @return
     */
    private boolean isParsed() {
        return ((status & IS_PARSED) == IS_PARSED);
    }

//    /**
//     * Answer whether logging is enabled for this request
//     * @return
//     */
//    public boolean isLoggingEnabled() {
//        return config.getProperty("logging.protocollMouseMoves.enabled", false, true);
//    }

    /**
     * Get part of the request uri which is actually identifying
     * the requested resource. The uri starts with a <b>/</b> which denotes the
     * context root.
     * 
     * @return "real" request uri
     */
    public String getRealRequestURI() {
        if (!isParsed()) parseRequest();
        return realRequestURI;
    }

    /**
     * Get get parameters which are encoded as part of the request uri
     * @return uriParameters
     */
    public String[] getUriParameters() {
        if (!isParsed()) parseRequest();
        return uriParameters;
    }
    
    /**
     * Returns the uri parameters as string
     * @return
     */
    public String getUriParameterString() {
        return StringUtils.join(getUriParameters(), "/");
    }
    
    /**
     * Get privileges required to enter the mapped page
     * @return requiredPrivileges
     */
    public List<Privilege> getRequiredPrivileges() {
        if (!isParsed()) parseRequest();
        return requiredPrivileges;
    }

//    /**
//     * Answer whether myself contains a session which needs
//     * to be initialized
//     * @return
//     */
//    public boolean hasUninitializedSession() {
//        return !visitorSession.isInitialized() && !getStatus(HAS_IGNORED_EXTENSION);
//    }

    /**
     * Answer whether this request should be dispatched
     * to its real destination
     * @return
     */
    public boolean shouldBeDispatched() {
        return (getStatus(HAS_REWRITTEN_URI) && !getStatus(IS_DISPATCHED));
    }
    
    /**
     * Performs a forward of myself based on the real request uri I stand for
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void dispatchToRealDestination(ServletResponse response) throws ServletException, IOException {
        
        getLogger().info("[" + getRealRequestURI() + "] Dispatching to real destination");
        setStatus(IS_DISPATCHED);
        
        getRequestDispatcher(getRealRequestURI()).forward(this, response);
    }

    /**
     * Answer the path in which the template, responsible to display the current
     * action, is located
     *
     * @return template path
     */
    public String getJspTemplatePath() {
        return "/content" + getRealRequestURI() + ".jsp";
    }

    /**
     * Process template for the given request. This mostly means, that the request
     * is framed in the /content/main.jsp page. In case of an XMLHttpRequest however,
     * the content is not framed, but directly included.
     * @param response
     */
    public void processTemplate(ServletResponse response) throws ServletException, IOException {
        // If response has been committed some other instance has 
        // answered the query. 
        if (response.isCommitted()) {
            getLogger().info("[" + getRealRequestURI() + "] Response has been committed. Skipping include of template");
            return;
        }

        // Make sure the request is only filtered once
        if (getStatus(IS_TEMPLATE_FILTERED)) {
            getLogger().info("[" + getRealRequestURI() + "] Template has already been filtered. Skipping include of template");
            return;
        }
        
        setStatus(IS_TEMPLATE_FILTERED | IS_DISPATCHED);
        
        if (isXMLHttpRequest() || isNoframeSet()) {
            getLogger().info("[" + getRealRequestURI() + "] Dispatching to real destination ('" + getJspTemplatePath() + "').");
            getRequestDispatcher(getJspTemplatePath()).forward(this, response);
        } else {
            getLogger().info("[" + getRealRequestURI() + "] Dispatching to main.jsp.");
            getRequestDispatcher("/content/main.jsp").forward(this, response);
        }
    }

    /**
     * Answer whether noframe parameter is set for request
     * @return
     */
    private boolean isNoframeSet() {
        return (getParameter("noframe") != null);
    }
    
    /**
     * Answer whether the current request is an XMLHttpRequest.
     * Get this information from request or flash.
     * @return
     */
    public boolean isXMLHttpRequest() {
        return ("XMLHttpRequest".equals(getHeader("X-Requested-With")));
    }
    
    /**
     * Extract info from the encapsulated request object
     */
    private void parseRequest() {
        // Get uri relative to webroot, skip trailing '/' if existing
        String relativeURI = getRequestURI().replaceFirst(getContextPath(), "");
        String[] parts = relativeURI.split("/");

        // Special treatment of context root
        if (parts.length == 0) {
            realRequestURI = "/" + pageMappingManager.getRootMapping();
            mappingURIPart = "/";
            setStatus(HAS_REWRITTEN_URI | IS_PARSED);
            return;
        }
        
        String lastRequestUriPart = parts[parts.length - 1];
        if (lastRequestUriPart.contains(".")) {
            String extension = lastRequestUriPart.substring(lastRequestUriPart.lastIndexOf("."));
            if (config.getProperty("de.nixis.vb.util.ignoredExtensions", ".jpg;.gif;.css;.js;.png;.ico;", true).contains(extension)) {
                setStatus(HAS_IGNORED_EXTENSION | IS_PARSED | IS_TEMPLATE_FILTERED | IS_DISPATCHED);
                realRequestURI = mappingURIPart = relativeURI;
                return;
            }
        }
        
        List<PageMapping> pageMappings = pageMappingManager.getPageMappingsByFirstUriComponent(parts[1]);
        
        // Get page associated with this request
        // In order to map a requestURI must be a subpath
        // of mapping or the mapping itself
        String mappingUri = null;
        for (PageMapping mapping: pageMappings) {
            mappingUri = "/" + mapping.getUri();
            // Request uri does not start with mapping... continue searching
            if (!relativeURI.startsWith(mappingUri)) continue;
            
            // Found page mapping
            // Set real request uri and rewrite directive
            realRequestURI = "/" + mapping.getMapping();
            if (!relativeURI.equals(realRequestURI)) setStatus(HAS_REWRITTEN_URI);
            
            // set required privileges
            requiredPrivileges = mapping.getPrivileges();
            
            // Finished
            break;
        }
        
        // If no page was assigned set default and disable 
        // template filtering (e.g. for images and stylesheets or
        // other data for which no template has been created)
        // This may also happen if the user tries to open a resource which does
        // not exist. In this case we do not want to perform any template
        // filtering or dispatching.
        if (realRequestURI == null) {
            realRequestURI = mappingUri = relativeURI;
            setStatus(IS_TEMPLATE_FILTERED | IS_DISPATCHED);
        }
        
        mappingURIPart = mappingUri;
        
        // Get request parameters
        String[] mappingUriParts = mappingUri.split("/");        
        uriParameters = Arrays.copyOfRange(parts, mappingUriParts.length, parts.length);
        
        // Get jumper for current request
        pathLevelJumper = StringUtils.repeat("../", StringUtils.countMatches(relativeURI, "/") - 1);
        setStatus(IS_PARSED);
    }
    
    /**
     * Returns whether the web app absolute uri (starting with a /) is the uri
     * of the current request
     * @param webappAbsoluteURI
     * @return
     */
    public boolean isCurrentPage(String webappAbsoluteURI) {
        return mappingURIPart.equals(webappAbsoluteURI);
    }
    
    /**
     * Formats an uri relative to the projects webroot to be relative
     * to the currently requested resource 
     * @param uri project webroot relative uri
     * @return request relative uri
     */
    public String formatUri(String uri) {
        return getPathLevelJumper() + uri;
    }
    
    /**
     * Return the path level jumper associated with this request
     * @return
     */
    public String getPathLevelJumper() {
        return pathLevelJumper;
    }

    /**
     * Returns the parameter identified by name or throws an exception if a) it is not found or b) equals ''
     * @param parameter name
     * @return
     * 
     * @throws ParameterNotFoundException if parameter is not specified
     */
    public String getParameterEx(String name) {        
        String s = getParameterOrNull(name);
        if (s == null) throw new ParameterNotFoundException("Parameter '" + name + "' does not exist");
        
        return s;
    }

    /**
     * Returns the value of param or null if it is an empty string or not specified
     * @param name
     * @return
     */
    public String getParameterOrNull(String name) {
        String s = getParameter(name);
        return (s == null || s.equals("") ? null : s);
    }
    
    /**
     * Returns the flash object associated with this request
     * A flash object can be used to set attributes on it.
     * Those will be avialiable only for the next request.
     *
     * It is usefull for passing data to pages to which is redirected.
     *
     * @return flash of the current request
     */
    public Flash getFlash() {
        return flash;
    }

    /**
     * Get cookie with the requested name
     * @param name
     * @return cookie
     */
    public Cookie getCookie(String name) {
        Cookie[] cookies = getCookies();
        if (cookies == null) return null;

        for (Cookie cookie: cookies) if (cookie.getName().equals(name)) return cookie;
        
        return null;
    }
    
//    /**
//     * Return the visitor session associated with this request
//     * @return visitor session
//     */
//    public VisitorSession getVisitorSession() {
//        return visitorSession;
//    }

    /**
     * Log some info about what was obtained from this wrapper
     */
    public void logRequestInfo() {
        if (!isParsed()) parseRequest();
        
        getLogger().info("[" + getRealRequestURI() + "] start log request info");
        if (!getStatus(HAS_IGNORED_EXTENSION)) {
            getLogger().info("[" + getRealRequestURI() + "] OriginalRequestURI: " + getRequestURI());
            getLogger().info("[" + getRealRequestURI() + "] PathLevelJumper: " + getPathLevelJumper());
            getLogger().info("[" + getRealRequestURI() + "] RealRequestURI: " + getRealRequestURI());
            getLogger().info("[" + getRealRequestURI() + "] UriParameters: " + Arrays.toString(uriParameters));
            getLogger().info("[" + getRealRequestURI() + "] JSPTemplatePath: " + getJspTemplatePath());
        }
        getLogger().info("[" + getRealRequestURI() + "] Status: [" +
            "REWRITTEN_URI: " + getStatus(HAS_REWRITTEN_URI) + ", " +
            "IGNORED_EXTENSION: " + getStatus(HAS_IGNORED_EXTENSION) + ", " +
            "TEMPLATE_FILTERED: " + getStatus(IS_TEMPLATE_FILTERED) + ", " +
            "DISPATCHED: " + getStatus(IS_DISPATCHED) + "]"
        );
    }

    /**
     * Returns the domainAddress
     *
     * @param withProtocol whether "http://" + domainAddress + "/" should be returned or not
     * @return
     */
    public String getDomainAddress(boolean withProtocol) {
        return (withProtocol ? "http://" : "") + config.getProperty("general.domain-address", "vb.nixis.de", true) + (withProtocol ? "/" : "");
    }

    /**
     * Get attribute from flash
     * @param name
     * @return
     */
    public Object getFlashAttribute(String name) {
        return flash.getAttribute(name);
    }

    /**
     * Sets attribute in flash
     * @param name
     * @param value
     */
    public void setFlashAttribute(String name, Object value) {
        flash.setAttribute(name, value);
    }

    /**
     * Sets the status of this request. May only add but
     * not remove information from the status of this request
     * 
     * @param status statuscode to set (an | connected int of IS_* constants)
     */
    protected void setStatus(int status) {
        this.status = this.status | status;
    }
    
    /**
     * Get status of specified property
     * @param status
     * @return
     */
    protected boolean getStatus(int status) {
        // Parse request if needed
        if (!isParsed()) parseRequest();

        return ((this.status & status) == status);
    }

    /**
     * Get logger
     * @return
     */
    protected Logger getLogger() {
        return logger;
    }
}
