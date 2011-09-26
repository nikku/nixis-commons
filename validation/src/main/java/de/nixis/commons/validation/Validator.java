/*
 * Part of knowledge engineering (ke) course work, 2010/11
 */
package de.nixis.commons.validation;

import java.util.HashMap;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 * @author markus.goetz
 * @author nico.rehwaldt
 */
public class Validator {
    
    private static HashMap<Class, javax.validation.Validator> validators = new HashMap();
    private static ValidatorFactory factory;
    
    static {
        Configuration<?> configuration = Validation.byDefaultProvider().configure();
        factory = configuration
            .messageInterpolator(new ContextualMessageInterpolator(configuration.getDefaultMessageInterpolator()))
            .buildValidatorFactory();
    }

    private static javax.validation.Validator getValidator(Class<?> cls) {
        if (!validators.containsKey(cls)) {
            validators.put(cls, factory.getValidator());
        }
        
        return validators.get(cls);
    }

    public static <T> ValidationResult<T> validate(T entity) {
        Set<ConstraintViolation<T>> violations = getValidator(entity.getClass()).validate(entity);
        return new ValidationResult<T>(violations);
    }
}