/*
 * Part of nixis.commons
 */
package de.nixis.commons.web.base.model;

import de.nixis.commons.digester.Digester;
import java.io.Serializable;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
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

    public enum Tokens {
        PASSWORD_RESET, 
        AUTH
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String name;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registered;

    @NotEmpty
    @Email
    @Column(unique = true)
    private String email;

    @JoinColumn(name="user_id")
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    private Set<Token> tokens = new HashSet<Token>();
    
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
     *                                  password hasType empty
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
    
    /**
     * Remove stored auth token
     */
    public <T extends Enum> Token<T> removeToken(T type) {
        Token token = getToken(type);
        if (token != null) {
            tokens.remove(token);
        }
        return token;
    }
    
    /**
     * Returns the value of the specified token or null
     */
    public <T extends Enum> Token<T> getToken(T type) {
        for (Token t: tokens) {
            if (t.hasType(type)) {
                return t;
            }
        }
        
        return null;
    }
    
    public <T extends Enum> String createToken(T type) {
        return createToken(type, null);
    }
    
    public <T extends Enum> String createToken(T type, String additionalData) {
        Token old = getToken(type);
        if (old != null) {
            tokens.remove(old);
        }
        
        Token<T> token = Token.<T>create(type, additionalData);
        tokens.add(token);
        return token.getValue();
    }
    
    /**
     * Clears this user's password.
     */
    public void clearPassword() {
        this.password = null;
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