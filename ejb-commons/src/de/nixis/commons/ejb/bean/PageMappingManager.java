package de.nixis.commons.ejb.bean;

import de.nixis.commons.ejb.domain.PageMapping;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Muffelbox
 */
@Local
public interface PageMappingManager {
    public List<PageMapping> getPageMappings();
    
    public PageMapping getPageMappingByUri(String uri);
    public PageMapping getReservedMappingByKey(String uri);
    
    public String getRootMapping();
    public String getErrorMapping();

    public List<PageMapping> getPageMappingsByFirstUriComponent(String component);
}