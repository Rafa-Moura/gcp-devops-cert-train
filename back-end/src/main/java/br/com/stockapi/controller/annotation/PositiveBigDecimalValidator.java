package br.com.stockapi.controller.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PositiveBigDecimalValidator implements ConstraintValidator<PositiveBigDecimal, BigDecimal> {

    @Override
    public void initialize(PositiveBigDecimal constraintAnnotation) {
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
}
