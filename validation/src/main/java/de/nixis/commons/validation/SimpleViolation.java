/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nixis.commons.validation;

import java.awt.geom.Path2D;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.engine.PathImpl;

/**
 * Very simple violation supporting nothing but a message
 * 
 * @author nico.rehwaldt
 */
public class SimpleViolation<T> implements ConstraintViolation<T> {
    
    private final String message;
    private final PathImpl path;

    public SimpleViolation(String propertyPath, String message) {
        this.path = PathImpl.createPathFromString(propertyPath);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return getMessage();
    }

    @Override
    public String getMessageTemplate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T getRootBean() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class<T> getRootBeanClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getLeafBean() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Path getPropertyPath() {
        return path;
    }

    @Override
    public Object getInvalidValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
