package com.demo.weatherMap.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

@Constraint(validatedBy = CityNameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD,PARAMETER })
public @interface CityName {

    String message() default "Invalid City Name value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
