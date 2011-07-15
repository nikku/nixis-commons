/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base.flash;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nico.rehwaldt
 */
public class FlashMapHelper {

    /**
     * Initializes flash map for a given request.
     * @param request
     */
    public static void initFlashMap(HttpServletRequest request) {
        HttpSession session = request.getSession();
        FlashMap flash = (FlashMap) session.getAttribute("__flashMap");
        if (flash != null) {
            for (Map.Entry<String, Object> entry: flash.getRequestAttributes().entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
        } else {
            session.setAttribute("__flashMap", new FlashMap());
        }
    }

    /**
     * Returns the flash map for a given request
     * @param request
     */
    public static FlashMap getFlashMap(HttpServletRequest request) {
        return (FlashMap) request.getSession().getAttribute("__flashMap");
    }
}
