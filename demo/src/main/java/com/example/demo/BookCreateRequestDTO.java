package com.example.demo;

import jakarta.validation.constraints.*;

public record BookCreateRequestDTO(
        @NotBlank(message = "title should not empty")
        @Size(max = 50, message = "title should be 50 characters at most")
        @Size(min = 3, message = "title should be at least 3 characters")
        String title,

        @NotBlank(message = "description should not be empty")
        @Size(max = 250, message = "description should be 250 characters at most")
        String description,

        @Min(value = 1, message = "number of copies should be at least 1")
        int copies,

        @DecimalMin(value = "0.0", inclusive = true, message = "rating must be at least 0.0")
        @DecimalMax(value = "5.0", inclusive = true, message = "rating must be at most 5.0")
        double rating,

        @NotBlank(message = "author should not be empty")
        @Size(max = 50, message = "author should be 50 characters at most")
        String author) {
}
