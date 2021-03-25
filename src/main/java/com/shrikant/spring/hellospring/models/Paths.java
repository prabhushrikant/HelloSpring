package com.shrikant.spring.hellospring.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class Paths {

  @JsonProperty("paths")
  private Map<String, FieldPath> properties;

  public Paths() {
    properties = new HashMap<>();
  }

  public Map<String, FieldPath> getProperties() {
    return properties;
  }

  @JsonAnySetter
  public void add(String property, FieldPath value) {
    properties.put(property, value);
  }
}
