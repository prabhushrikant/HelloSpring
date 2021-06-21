package com.shrikant.spring.hellospring.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = {TenantsValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantString {

  String message() default "Invalid tenant string can only contain alpha numeric keys and .";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
