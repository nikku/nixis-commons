/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nixis.commons.web.flash.jersey;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import de.nixis.commons.web.flash.FlashMap;
import de.nixis.commons.web.flash.FlashHelper;
import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author nico.rehwaldt
 */
@Provider
public class FlashMapProvider implements InjectableProvider<Context, Type>, 
                                         Injectable<FlashMap> {
    
    @Context
    private ThreadLocal<HttpServletRequest> tlr;
    
    @Override
    public Injectable<FlashMap> getInjectable(ComponentContext ic, Context a, Type c) {
        if (c.equals(FlashMap.class)) {
            return this;
        }
        return null;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }
    
    @Override
    public FlashMap getValue() {
        HttpServletRequest request = tlr.get();
        if (request == null) {
            throw new IllegalStateException("Outside a request scope");
        }
        
        return FlashHelper.getFlash(request);
    }
}
