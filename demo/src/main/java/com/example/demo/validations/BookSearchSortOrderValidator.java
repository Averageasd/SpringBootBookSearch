package com.example.demo.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class BookSearchSortOrderValidator implements ConstraintValidator<ValidateBookSearchSortOrder, String> {
    @Override
    public boolean isValid(String bookSearchSortOrder, ConstraintValidatorContext constraintValidatorContext) {
        List<String> validSortOrders = Arrays.asList("ASC", "DESC");
        return validSortOrders.contains(bookSearchSortOrder);
    }
}
