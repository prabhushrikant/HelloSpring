package com.shrikant.spring.hellospring.validators;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TenantsValidator implements ConstraintValidator<TenantString, String> {

  private static final Predicate<String> ALPHA_NUM_ONLY = Pattern.compile("^[A-Za-z0-9]*$").asPredicate();

  @Override
  public void initialize(TenantString constraintAnnotation) {

  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return ALPHA_NUM_ONLY.test(value);
  }
}
