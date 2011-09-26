/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.flash;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Some helper methods to extract a flash map from the request scope
 * @author nico.rehwaldt
 */
public class FlashHelper {
    
    public static final String FLASH_SESSION_STORAGE = "__flashMap";
    
    /**
     * Initializes a {@link FlashMap} for the given 
     * request and returns the newly map. 
     * 
     * @param request
     */
    public static FlashMap initFlash(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            throw new IllegalStateException("Session not initialized");
        }
        
        FlashMap flash = (FlashMap) session.getAttribute(FLASH_SESSION_STORAGE);
        if (flash != null) {
            // Get entries stored in flash and put them into 
            // the current request
            for (Map.Entry<String, Object> entry: flash.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
            
            // Clear the flash
            flash.clear();
        } else {
            session.setAttribute(FLASH_SESSION_STORAGE, new FlashMap());
        }
        
        return flash;
    }

    /**
     * Returns the getFlash map for a given request
     * @param request
     */
    public static FlashMap getFlash(HttpServletRequest request) {
        FlashMap map = (FlashMap) request.getSession().getAttribute(FLASH_SESSION_STORAGE);
        
        if (map == null) {
            map = new FlashMap();
        }
        
        return map;
    }
}
