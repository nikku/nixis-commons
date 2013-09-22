/**
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package de.nixis.commons.web.flash;

import java.util.Map;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ContextResolver;

/**
 * A resolver for {@link Flash} contexts.
 *
 * Allows <code>@Context Flash flash</code>.
 *
 * @author nico.rehwaldt
 */
@Provider
public class FlashMapResolver implements ContextResolver<Flash> {

  @Override
  public Flash getContext(Class<?> type) {
    return new ThreadLocalFlash();
  }

  private static class ThreadLocalFlash implements Flash {

    @Override
    public void put(String name, Object object) {
      FlashHolder.get().put(name, object);
    }

    @Override
    public Object get(String name) {
      return FlashHolder.get().get(name);
    }

    @Override
    public void putAll(Map<String, Object> values) {
      FlashHolder.get().putAll(values);
    }

    @Override
    public void clear() {
      FlashHolder.get().clear();
    }
  }
}
