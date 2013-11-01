package de.nixis.common.validation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nico
 */
public class MyMoreComplexEntity {

  @NotNull
  private Long id;

  @NotNull
  private String name;

  @Valid
  private MyEntity entity;

  public MyMoreComplexEntity(Long id, String name, String subEntityName) {
    this.id = id;
    this.name = name;
    this.entity = new MyEntity(subEntityName);
  }
}
