package de.nixis.common.base.model;

import de.nixis.common.digester.RandomGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author nico.rehwaldt
 */
@Entity
public class Token<T extends Enum> {

  @Id
  @GeneratedValue
  private Integer id;

  @NotNull
  private String type;

  @NotEmpty
  private String value;

  private String additionalData;

  public Token() {
  }

  private Token(T type, String value, String additionalData) {
    this.type = type.toString();
    this.value = value;
    this.additionalData = additionalData;
  }

  public String getAdditionalData() {
    return additionalData;
  }

  /**
   * Return true if this token has the specified type false otherwise
   */
  public boolean hasType(T t) {
    return t.name().equals(type);
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Token && obj.hashCode() == hashCode());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
    return hash;
  }

  public static <T extends Enum> Token<T> create(T type, String additionalData) {
    return new Token(type, createRandomString(), additionalData);
  }

  public static <T extends Enum> Token<T> create(T type) {
    return create(type, null);
  }

  public static <T extends Enum> Token<T> empty(T t) {
    return new Token<T>(t, null, null);
  }

  /**
   * Return a new random string
   *
   * @return
   */
  private static String createRandomString() {
    return RandomGenerator.nextRandom();
  }
}
