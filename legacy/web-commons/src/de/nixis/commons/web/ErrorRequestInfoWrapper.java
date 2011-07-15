package de.nixis.commons.web;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Dummdoedel
 */
public class ErrorRequestInfoWrapper extends RequestInfoWrapper {

    String pathLevelJumper;
    
    /**
     * Wraps an request object
     * @param request
     */
    public ErrorRequestInfoWrapper(HttpServletRequest request, String pathLevelJumper) {
        super(request);

        this.pathLevelJumper = pathLevelJumper;
    }

    /**
     * Returns the path level jumper associated with this request
     * @param uri project webroot relative uri
     * @return request relative uri
     */
    @Override
    public String getPathLevelJumper() {
        return pathLevelJumper;
    }
}