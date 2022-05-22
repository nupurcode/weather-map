package com.demo.weatherMap.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryCodeValidator implements ConstraintValidator<CountryCode, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return  value != null && value.matches("^[a-z]{2}$");
    }
}
