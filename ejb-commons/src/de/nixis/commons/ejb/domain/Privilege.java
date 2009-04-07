package de.nixis.commons.ejb.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.hibernate.validator.NotNull;

/**
 *
 * @author Muffelbox
 */
@Entity
@NamedQueries(value={
    @NamedQuery(name="Privilege.getByName", query="SELECT p FROM Privilege p WHERE p.name = :name")
})
public class Privilege implements Serializable {
    private static final long serialVersionUID = 13358123174924L;
    
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name="name", unique=true)
    private String name;
    
    @Column(name="description")
    private String description;
    
    public Privilege() {}
    public Privilege(String name) {
        this(name, null);
    }
    public Privilege(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Privilege)) {
            return false;
        }
        Privilege other = (Privilege) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "de.nixis.commons.ejb.domain.Privilege[id=" + id + "]";
    }

}
