package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookUpdateRequestDTO(
        @NotBlank(message = "title should not empty")
        @Size(max = 50, message = "title should be 50 characters at most")
        @Size(min = 3, message = "title should be at least 3 characters")
        String title,

        @NotBlank(message = "description should not be empty")
        @Size(max = 250, message = "description should be 250 characters at most")
        String description
) { }
