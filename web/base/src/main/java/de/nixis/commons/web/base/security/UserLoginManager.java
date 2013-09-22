/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base.security;

import de.nixis.commons.web.base.model.UserBase;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User manager component which can log in a user based on an auth token or on login credentials
 *
 * @author nico.rehwaldt
 */
@Component
public class UserLoginManager {

  @PersistenceContext
  private EntityManager em;

  /**
   * Tries to login a user with the specified auth token.
   *
   * @param authToken to be used
   *
   * @return the user or null if login was not successful
   */
  @Transactional
  public UserBase loginViaAuthToken(String authToken) {
    try {
      UserBase user = (UserBase) em.createQuery("SELECT u FROM User u JOIN u.tokens t WHERE t.value = :authToken AND t.type = :type")
          .setParameter("authToken", authToken)
          .setParameter("type", "AUTH")
          .getSingleResult();

      user.setLastLogin(new Date());
      return user;
    } catch (NoResultException e) {
      // user with auth token was not found in db
    }

    return null;
  }

  /**
   * Login a user with the specified login credentials.
   *
   * @param name the user name
   * @param password the password for the account
   *
   * @return the user or null if login was not successful
   */
  @Transactional
  public UserBase loginWithCredentials(String name, String password) {
    try {
      UserBase user = (UserBase) em.createNamedQuery("User.GetByName")
          .setParameter("name", name)
          .getSingleResult();

      if (user.passwordEquals(password)) {
        user.setLastLogin(new Date());
        return user;
      }
    } catch (NoResultException e) {
      // user with specified name was not found in db
    }

    return null;
  }
}
