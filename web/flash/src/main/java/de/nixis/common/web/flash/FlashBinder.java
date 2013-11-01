package de.nixis.common.web.flash;


import org.glassfish.hk2.api.PerLookup;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.internal.inject.ReferencingFactory;
import org.glassfish.jersey.internal.util.collection.Ref;
import org.glassfish.jersey.process.internal.RequestScoped;

/**
 *
 * @author nico.rehwaldt
 */
public class FlashBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bindFactory(FlashReferencingFactory.class).to(Flash.class).in(PerLookup.class);
    bindFactory(ReferencingFactory.<Flash>referenceFactory()).to(new TypeLiteral<Ref<Flash>>() {}).in(RequestScoped.class);
  }
}
