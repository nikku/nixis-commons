package de.nixis.commons.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * This exception is thrown if validation errors occurred.
 * 
 * @author Jan Rehwaldt
 * @since 2011-07-14
 */
public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 6577451214125925205L;
    
    private Object entity;
    private ValidationResult result;
    
    /**
     * Creates a validation exception from a single violation
     * 
     * @param message
     * @param entity 
     */
    public ValidationException(String property, String message, Object entity) {
        this("Invalid entity", entity, new ValidationResult());
        
        result.addViolation(property, message);
    }
    
    public ValidationException(String message, Object entity, ValidationResult result) {
        super(message);
        
        this.entity = entity;
        this.result = result;
    }
    
    /**
     * Provides the errors, which caused the exception.
     * 
     * @return the errors
     */
    public Set<ConstraintViolation<Object>> getErrors() {
        return result.getViolations();
    }
    
    /**
     * Provides the invalid object.
     * 
     * @return the invalid object
     */
    public Object getInvalidEntity() {
        return this.entity;
    }
    
    /**
     * Return validation result based on the exceptions found
     */
    public ValidationResult getValidationResult() {
        return result;
    }
}
