package de.nixis.commons.ejb.bean;

import de.nixis.commons.ejb.GenericBean;
import de.nixis.commons.ejb.domain.PageMapping;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

/**
 *
 * @author Muffelbox
 */
@Stateless(
   mappedName="PageMappingManager",
   name="PageMappingManager",
   description="Manages the page mappings"
)
public class PageMappingManagerBean extends GenericBean implements PageMappingManager {
    /**
     * Looks up a page mapping based on its uri
     * @param name the uri
     * @return the page mapping or null if page mapping was not found
     */
    public PageMapping getPageMappingByUri(String uri) {
        try {
            return (PageMapping) em.createNamedQuery("PageMapping.getByUri").setParameter("uri", uri).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Get PageMappings which have a specific first part
     * @param component
     * @return list of mappings, ordered by their length
     */
    @SuppressWarnings("unchecked")
    public List<PageMapping> getPageMappingsByFirstUriComponent(String component) {
        return (List<PageMapping>) em.createNamedQuery("PageMapping.getByFirstUriComponent").setParameter("uriPart", component + "%").getResultList();
    }
    
    /**
     * Looks up a reserved mapping based on its key
     * @param name the key
     * @return the reserved mapping or null if reserved mapping was not found
     */
    public PageMapping getReservedMappingByKey(String key) {
        try {
            return (PageMapping) em.createNamedQuery("PageMapping.getByKey").setParameter("key", key).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Returns a list of all page mappings
     * @return the page mappings
     */
    @SuppressWarnings("unchecked")
    public List<PageMapping> getPageMappings() {
        return (List<PageMapping>) em.createNamedQuery("PageMapping.getAll").getResultList();
    }

    /**
     * Looks up the root mapping (reserved keyword: 'root', may be overwritten in config)
     * @return the root mapping
     */
    public String getRootMapping() {
        PageMapping rootMapping = getReservedMappingByKey(
                getConfig().getProperty("de.nixis.vb.bean.PageMappingManager.rootMappingKey", "_root", true)
        );
        return rootMapping != null
                ? rootMapping.getMapping()
                : getConfig().getProperty("de.nixis.vb.bean.PageMappingManager.rootMappingDefaultValue", "start", true);
    }

    /**
     * Looks up the error mapping (reserved keyword: 'error', may be overwritten in config)
     * @return the error mapping
     */
    public String getErrorMapping() {
        PageMapping errorMapping = getReservedMappingByKey(
                getConfig().getProperty("de.nixis.vb.bean.PageMappingManager.errorMappingKey", "_error", true)
        );
        return errorMapping != null
                ? errorMapping.getMapping()
                : getConfig().getProperty("de.nixis.vb.bean.PageMappingManager.errorMappingDefaultValue", "fehler", true);
    }
}
