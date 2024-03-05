package br.com.stockapi.controller.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositiveBigDecimalValidator.class)
public @interface PositiveBigDecimal {

    String message() default "O valor deve ser maior que 0.0";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
