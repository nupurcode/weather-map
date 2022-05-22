package com.demo.weatherMap.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CityNameValidator implements ConstraintValidator<CityName, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return value != null && value.matches("^[A-Za-z]{2,}$");
    }
}
