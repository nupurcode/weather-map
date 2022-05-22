package com.demo.weatherMap.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

@Constraint(validatedBy = CountryCodeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD,PARAMETER })
public @interface CountryCode {

    String message() default "Invalid Country code value - must be of two characters only";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
