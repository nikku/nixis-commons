/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base.model;

import de.nixis.commons.digester.Digester;
import java.io.Serializable;
import java.security.Principal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

    @NotEmpty
    private String name;

    /**
     * A token to authenticate a user via non-cookie media
     */
    private String authToken;

    /**
     * A token to authenticate password reset
     */
    private String passwordResetToken;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registered;

    @NotEmpty
    @Email
    @Column(unique = true)
    private String email;

    private String password;

    public UserBase() {}

    public UserBase(String name, String email) {
        this.name = name;
        this.email = email;
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

    /**
     * Sets the password for this user
     * 
     * @param password
     * @param verification 
     * 
     * @throws IllegalArgumentException if password and verification do not match or
     *                                  password is empty
     */
    public final void setPassword(String password, String verification) {
        if (password == null || 
            password.isEmpty() || 
            !password.equals(verification)) {
            
            throw new IllegalArgumentException("Password and verification do not match");
        }
        
        this.password = Digester.digest(password);
    }
    
    /**
     * @param other the password to use as comparison
     * @return true if user stored password equals the argument
     */
    public boolean passwordEquals(String other) {
        return Digester.matches(other, password);
    }

    private String createToken() {
        String s = new StringBuilder()
                .append(id)
                .append(password)
                .append(Math.random())
                .append(name)
                .append(new Date())
                .toString();
        
        return s;
    }
    
    /**
     * Returns a new auth token and stores it internally
     * @return the new auth token
     */
    public String createAuthToken() {
        authToken = Digester.hash(createToken());
        return authToken;
    }

    /**
     * Returns a new token to reset a users password
     * @return the new password reset token
     */
    public String createPasswordResetToken() {
        passwordResetToken = Digester.hash(createToken());
        return passwordResetToken;
    }
    
    /**
     * Returns a previously created auth token for the current user
     * @return 
     */
    public String getAuthToken() {
        return authToken;
    }
    
    /**
     * Returns a previously created password reset token for the current user
     * @return 
     */
    public String getPasswordResetToken() {
        return passwordResetToken;
    }
    
    /**
     * Remove stored auth token
     */
    public void removeAuthToken() {
        this.authToken = null;
    }
    
    /**
     * Remove stored password reset token
     */
    public void removePasswordResetToken() {
        this.passwordResetToken = null;
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