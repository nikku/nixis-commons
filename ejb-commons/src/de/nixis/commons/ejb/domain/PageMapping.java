package de.nixis.commons.ejb.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.hibernate.validator.AssertTrue;
import org.hibernate.validator.NotNull;

/**
 *
 * @author Muffelbox
 */
@Entity
@NamedQueries(value = {
    @NamedQuery(name = "PageMapping.getByUri", query = "SELECT DISTINCT m FROM PageMapping m LEFT JOIN FETCH m.requiredPrivileges WHERE m.uri = :uri AND m.reserved = false"),
    @NamedQuery(name = "PageMapping.getByFirstUriComponent", query = "SELECT DISTINCT m FROM PageMapping m LEFT JOIN FETCH m.requiredPrivileges WHERE m.uri LIKE :uriPart ORDER BY LENGTH(m.uri) DESC"),
    @NamedQuery(name = "PageMapping.getByKey", query = "SELECT m FROM PageMapping m WHERE m.uri = :key AND m.reserved = true"),
    @NamedQuery(name = "PageMapping.getAll", query = "SELECT DISTINCT m FROM PageMapping m LEFT JOIN FETCH m.requiredPrivileges WHERE m.reserved = false ORDER BY m.uri")
})
public class PageMapping implements Serializable {
    private static final long serialVersionUID = 1424214244545668L;
    
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name="uri", unique=true)
    private String uri;

    @NotNull
    @Column(name="mapping")
    private String mapping;
    
    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinColumn(name="privilegeID")
    private List<Privilege> requiredPrivileges = new ArrayList<Privilege>();
    
    private Boolean reserved = false;
    
    /**
     * Non-arg public constructor for EJB3 layer
     */
    public PageMapping() {}
    
    public PageMapping(String uri, String mapping) {
        this(uri, mapping, false);
    }
    public PageMapping(String uri, String mapping, boolean reserved) {
        this.uri = uri;
        this.mapping = mapping;
        this.reserved = reserved;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getMapping() {
        return mapping;
    }
    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    /**
     * Checks constraint, that only non-reserved mappings may have privileges
     * @return
     */
    @AssertTrue
    private boolean assertNoPrivilegesIfReserved() {
        return (!reserved || requiredPrivileges.isEmpty());
    }

    public void assignPrivileges(List<Privilege> privileges) {
        this.requiredPrivileges = privileges;
    }
    public List<Privilege> getPrivileges() {
        return requiredPrivileges;
    }

    public void assignPrivilege(Privilege privilege) {
        this.requiredPrivileges.add(privilege);
    }

    public void removePrivilege(Privilege privilege) {
        this.requiredPrivileges.remove(privilege);
    }
    
    public Boolean isReserved() {
        return reserved;
    }
    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PageMapping)) {
            return false;
        }
        PageMapping other = (PageMapping) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.nixis.commons.ejb.domain.PageMapping[id=" + id + "]";
    }
}
