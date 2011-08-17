/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base.model;

import de.nixis.commons.digester.Digester;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Basic user class which provides basis for other users.
 * 
 * @author nico.rehwaldt
 */
@MappedSuperclass
public abstract class UserBase implements Serializable, Principal {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "You need to assign a user name")
    private String name;

    /**
     * A token to authenticate a user via cookies or other media
     */
    private String authToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registered;

    @NotEmpty(message = "You need to fill in your e-mail address")
    @Email(message = "The e-mail address you entered is not valid")
    private String email;

    @Transient
    private String unencryptedPassword;

    @Transient
    private String unencryptedPasswordVerification;

    private String password;

    public UserBase() {}

    public UserBase(String name, String email, String password,
                String passwordVerification) {
        
        this.name = name;
        this.email = email;

        setPassword(password, passwordVerification);
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUnencryptedPassword() {
        return unencryptedPassword;
    }

    public String getUnencryptedPasswordVerification() {
        return unencryptedPasswordVerification;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Return true if the user has the specified role 
     * 
     * @return true, if user has the specified role, false otherwise
     */
    public abstract boolean hasRole(String role);
    
    public void setEmail(String email) {
        this.email = email;
    }

    public final void setPassword(String password, String verification) {
        unencryptedPassword = password;
        unencryptedPasswordVerification = verification;
    }

    public boolean isPasswordValid() {
        if (unencryptedPassword != null) {
            return !unencryptedPassword.equals("");
        }

        return false;
    }

    public boolean verificationMatches() {
        if (unencryptedPassword != null) {
            return unencryptedPassword.equals(unencryptedPasswordVerification);
        }

        return false;
    }

    @PrePersist
    @PreUpdate
    protected void encryptPassword() throws NoSuchAlgorithmException {
        if (unencryptedPassword != null) {
            this.password = Digester.digest(unencryptedPassword);
        }
    }
    
    /**
     * @param other the password to use as comparison
     * @return true if user stored password equals the argument
     */
    public boolean passwordEquals(String other) {
        return Digester.matches(other, password);
    }

    /**
     * Returns a new auth token and stores it on the user instance
     * @return auth token
     */
    public String createAuthToken() {
        String s = new StringBuilder()
                .append(id)
                .append(password)
                .append(new Date())
                .toString();

        authToken = Digester.hash(s);
        return authToken;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserBase)) {
            return false;
        }
        UserBase other = (UserBase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.nixis.commons.web.base.model.UserBase[id=" + id + "]";
    }
}