package com.shrikant.spring.hellospring.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackDependency {

  /**
   * Retrieve a resourceOwner.
   */
  ResourceOwner resourceOwner() default ResourceOwner.NOT_SPECIFIED;


  /**
   * Retrieve a resourceType.
   */
  ResourceType resourceType() default ResourceType.NOT_SPECIFIED;

  /**
   * Retrieve commandName.
   */
  String commandName() default "";
}
