package de.nixis.commons.ejb.bean;

import de.nixis.commons.ejb.GenericBean;
import de.nixis.commons.ejb.domain.Privilege;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

/**
 *
 * @author Dummdoedel
 */
@Stateless(
    name="PrivilegeManager",
    mappedName="PrivilegeManager",
    description="Manages privileges"
)
public class PrivilegeManagerBean extends GenericBean implements PrivilegeManager {
    /**
     * Lookup a privilege by name
     * @param name
     * @return privilege instance or null if privilege was not found
     */
    @Override
    public Privilege getPrivilegeByName(String name) {
        try {
            return (Privilege) em.createNamedQuery("Privilege.getByName").setParameter("name", name).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null;
        }
    }
}
