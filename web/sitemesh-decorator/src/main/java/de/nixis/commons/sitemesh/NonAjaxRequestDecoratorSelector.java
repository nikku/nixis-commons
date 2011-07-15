/*
 * Part of nixis.commons
 */
package de.nixis.commons.sitemesh;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.sitemesh.DecoratorSelector;
import org.sitemesh.content.Content;
import org.sitemesh.webapp.WebAppContext;

/**
 * Custom sitemesh decorator which respects usage of
 * X-Requested-With header and uses no layout if request is done using ajax.
 * 
 * @author nico.rehwaldt
 */
public class NonAjaxRequestDecoratorSelector implements DecoratorSelector<WebAppContext> {

    private final DecoratorSelector<WebAppContext> parent;

    /**
     * Instantiates the decorator selector with a given parent, which will be
     * used when the request is normal.
     * 
     * @param parent used as fallback if request is done the normal (non-ajax)
     *        way
     */
    public NonAjaxRequestDecoratorSelector(DecoratorSelector<WebAppContext> parent) {
        this.parent = parent;
    }

    /**
     * Based on current request, returns the parent view selector if request
     * is made the normal way. If it is made via ajax, no view selector is returned.
     * 
     * @param content
     * @param c
     * @return
     * @throws IOException
     */
    @Override
    public String[] selectDecoratorPaths(Content content, WebAppContext c) throws IOException {
        if (!isAjax(c.getRequest())) {
            return parent.selectDecoratorPaths(content, c);
        } else {
            return new String[0];
        }
    }

    /**
     * Returns true if request is made using ajax.
     * 
     * @param request to check
     *
     * @return true if request is ajax, false otherwise
     */
    private static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}