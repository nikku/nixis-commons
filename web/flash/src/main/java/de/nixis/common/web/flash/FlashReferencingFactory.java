package de.nixis.common.web.flash;


import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.ext.ContextResolver;

import org.glassfish.jersey.internal.inject.ReferencingFactory;
import org.glassfish.jersey.internal.util.collection.Ref;

/**
 * A {@link ContextResolver} for {@link Flash} contexts.
 *
 * Allows <code>@Context Flash flash</code>.
 *
 * @author nico.rehwaldt
 */
public class FlashReferencingFactory extends ReferencingFactory<Flash> {

  @Inject
  public FlashReferencingFactory(Provider<Ref<Flash>> referenceFactory) {
    super(referenceFactory);
  }
}
