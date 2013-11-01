package de.nixis.common.validation;

import javax.validation.constraints.NotNull;

/**
 *
 * @author nico
 */
public class MyEntity {

  @NotNull
  private String key;

  public MyEntity(String key) {
    this.key = key;
  }
}
