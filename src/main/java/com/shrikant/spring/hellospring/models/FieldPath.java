package com.shrikant.spring.hellospring.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.beans.ConstructorProperties;

public class FieldPath {

  @JsonProperty("fieldPath")
  public final String fieldPath;

  @ConstructorProperties({"fieldPath"})
  public FieldPath(String fieldPath) {
    this.fieldPath = fieldPath;
  }

}
