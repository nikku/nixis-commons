package de.nixis.commons.ejb;

import java.util.HashMap;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;

/**
 * This utility class can be used to obtain a class
 * validator to check validation constraints on domain objects
 * @author Dummdoedel
 */
public class Validator {
    
    private static HashMap<Class, ClassValidator> validators = new HashMap<Class, ClassValidator>();

    /**
     * Assert the correctness of o
     * @param o
     * 
     * @throws InvalidStateException if invalid fields were found
     */
    public static void assertValid(Object o) throws InvalidStateException {
        Class clazz = o.getClass();
        getValidator(clazz).assertValid(o);
    }

    /**
     * Get Entity bean validator for given entity class
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> ClassValidator<T> getValidator(Class<T> clazz) {
        if (!validators.containsKey(clazz)) {
            ClassValidator<T> validator = new ClassValidator(clazz);
            validators.put(clazz, validator);
        }

        return (ClassValidator<T>) validators.get(clazz);
    }
}