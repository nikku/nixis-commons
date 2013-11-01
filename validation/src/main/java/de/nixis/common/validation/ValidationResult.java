package de.nixis.common.validation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 *
 * @author nico.rehwaldt
 */
public class ValidationResult<T> {

  private Set<ConstraintViolation<T>> violations;

  public ValidationResult() {
    this(new HashSet<ConstraintViolation<T>>());
  }

  public ValidationResult(Set<ConstraintViolation<T>> violations) {
    this.violations = violations;
  }

  public Set<ConstraintViolation<T>> getViolations() {
    return violations;
  }

  /**
   * Returns true if no violations are stored in this result
   *
   * @return
   */
  public boolean isEmpty() {
    return violations.isEmpty();
  }

  /**
   * Add the specified message as violation to the user
   *
   * @param message
   */
  public void addViolation(String message) {
    addViolation("", message);
  }

  /**
   * Add the specified message as violation to the user
   *
   * @param message
   */
  public void addViolation(String path, String message) {
    violations.add(new SimpleViolation<T>(path, message));
  }

  /**
   * Merges the given other result into this validation result
   *
   * @param otherResult
   */
  public void merge(ValidationResult<T> otherResult) {
    violations.addAll(otherResult.getViolations());
  }

  /**
   * Creates an empty immutable {@link ValidationResult}.
   *
   * @param <T> the constraint's type
   * @return the empty result
   */
  public static <T extends Object> ValidationResult<T> empty() {
    return new ValidationResult<T>(Collections.<ConstraintViolation<T>>emptySet());
  }
}
