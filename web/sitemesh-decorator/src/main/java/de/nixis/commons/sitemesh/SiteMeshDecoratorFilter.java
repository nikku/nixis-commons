package de.nixis.commons.sitemesh;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

/**
 * Decorator filter for Sitemesh 3 which initializes sitemesh to use the
 * {@link NonAjaxRequestDecoratorSelector}.
 *
 * The layout used by this decorator may be specified via the
 * `de.nixis.commons.sitemesh-decorator.layout` init parameter for
 * this filter. It defaults to `/WEB-INF/views/layout.jsp`.
 *
 * This filter may be customized to special needs.
 *
 * @see SiteMeshDecoratorFilter#registerDecoratorPaths(org.sitemesh.builder.SiteMeshFilterBuilder)
 * @see SiteMeshDecoratorFilter#registerDecoratorSelectors(org.sitemesh.builder.SiteMeshFilterBuilder)
 * @see SiteMeshDecoratorFilter#setMimeTypes(org.sitemesh.builder.SiteMeshFilterBuilder)
 *
 * @author nico.rehwaldt
 */
public class SiteMeshDecoratorFilter extends ConfigurableSiteMeshFilter {

  /**
   * Name of init parameter to specifiy layout.
   */
  public static final String LAYOUT = "de.nixis.commons.sitemesh-decorator.layout";

  /**
   * Default layout path used by the filter
   */
  public static final String DEFAULT_LAYOUT_PATH = "/WEB-INF/views/layout.jsp";

  /**
   * Layout used by the filter, defaults to {@link DEFAULT_LAYOUT_PATH}
   */
  private String layout = DEFAULT_LAYOUT_PATH;

  /**
   * Initializes the filter
   *
   * @param config
   * @throws ServletException
   */
  @Override
  public void init(FilterConfig config) throws ServletException {
    super.init(config);

    String path = config.getInitParameter(LAYOUT);
    if (path != null) {
      layout = path;
    }
  }

  /**
   * Plugs in the ajax functionality. The behaviour may be configured in subclasses of this filter.
   *
   * @see SiteMeshDecoratorFilter#registerDecoratorPaths(org.sitemesh.builder.SiteMeshFilterBuilder)
   * @see SiteMeshDecoratorFilter#registerDecoratorSelectors(org.sitemesh.builder.SiteMeshFilterBuilder)
   * @see SiteMeshDecoratorFilter#setMimeTypes(org.sitemesh.builder.SiteMeshFilterBuilder)
   *
   * @param builder the builder to apply the configuration to
   */
  @Override
  public void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
    registerDecoratorPaths(builder);
    setMimeTypes(builder);
    registerDecoratorSelectors(builder);
  }

  /**
   * Register decorator paths for the application by specifying layout files for one or more url patterns.
   *
   * The default behaviour is to register the default layout (see {@link SiteMeshDecoratorFilter}) on all paths (`/*`).
   *
   * @param builder the builder to apply the configuration to
   */
  protected void registerDecoratorPaths(SiteMeshFilterBuilder builder) {
    builder.addDecoratorPath("/*", layout);
  }

  /**
   * Set mime types recognized by sitemesh.
   *
   * The default behaviour is to regiter the text/html mime type to be intercepted by sitemesh.
   *
   * @param builder the builder to apply the configuration to
   */
  protected void setMimeTypes(SiteMeshFilterBuilder builder) {
    builder.setMimeTypes("text/html");
  }

  /**
   * Register decorator selectors used by the application.
   *
   * The default behaviour is to register the {@link NonAjaxRequestDecoratorSelector}.
   *
   * @param builder the builder to apply the configuration to
   */
  protected void registerDecoratorSelectors(SiteMeshFilterBuilder builder) {
    builder.setCustomDecoratorSelector(
        new NonAjaxRequestDecoratorSelector(builder.getDecoratorSelector()));
  }
}
