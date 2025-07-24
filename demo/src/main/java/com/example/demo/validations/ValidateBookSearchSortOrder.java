package com.example.demo.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BookSearchSortOrderValidator.class)
public @interface ValidateBookSearchSortOrder {
    public String message() default "Invalid sort order. It should be ASC or DESC";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
