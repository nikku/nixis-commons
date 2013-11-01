/*
 * Part of knowledge engineering (ke) course work, 2010/11
 */
package de.nixis.common.validation;

import java.util.HashMap;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 * Simple interface to means provided by {@link javax.validation}.
 *
 * @author nico.rehwaldt
 */
public class Validator {

  private static boolean cache = true;

  private HashMap<Class, javax.validation.Validator> validators;

  private ValidatorFactory factory;

  public Validator(Configuration<?> config) {
    factory = config.messageInterpolator(getInterpolator(config))
        .buildValidatorFactory();

    validators = new HashMap<Class, javax.validation.Validator>();
  }

  private MessageInterpolator getInterpolator(Configuration<?> config) {
    MessageInterpolator i = config.getDefaultMessageInterpolator();
    return new ContextualMessageInterpolator(i);
  }

  private javax.validation.Validator getValidator(Class<?> cls) {
    if (!(cache && validators.containsKey(cls))) {
      validators.put(cls, factory.getValidator());
    }

    return validators.get(cls);
  }

  private static Validator validator;

  /**
   * Returns the singleton validator
   *
   * @return
   */
  private static synchronized Validator getInstance() {
    if (validator == null) {
      Configuration<?> c = Validation.byDefaultProvider().configure();
      validator = new Validator(c);
    }

    return validator;
  }

  /**
   * Validate a given entity and return a validation result
   *
   * @param <T>
   * @param entity
   *
   * @return
   */
  public static <T> ValidationResult<T> validate(T entity, Class<?>... groups) {
    Set<ConstraintViolation<T>> violations = getInstance().getValidator(entity.getClass()).validate(entity, groups);
    return new ValidationResult<>(violations);
  }
}