package com.example.demo.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class BookSearchSortColumnValidator implements ConstraintValidator<ValidateBookSearchSortColumn, String> {
    @Override
    public boolean isValid(String sortColumn, ConstraintValidatorContext constraintValidatorContext) {
        List<String> validSortOrders = Arrays.asList("created_at", "copies", "rating", "title");
        return validSortOrders.contains(sortColumn);
    }
}
