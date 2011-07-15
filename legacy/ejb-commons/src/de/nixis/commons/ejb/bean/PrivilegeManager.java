package de.nixis.commons.ejb.bean;

import de.nixis.commons.ejb.domain.Privilege;

import javax.ejb.Local;

/**
 *
 * @author Muffelbox
 */
@Local
public interface PrivilegeManager {
    public Privilege getPrivilegeByName(String name);
}