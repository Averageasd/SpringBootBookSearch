package com.example.demo.validations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BookSearchSortColumnValidator.class)
public @interface ValidateBookSearchSortColumn {
    public String message() default "Invalid sort column. It should be one of these: created_at, copies, rating, title";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
