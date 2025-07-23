package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookPaginationSearchDTO(
    @NotNull(message = "page must not be null")
    Integer page,

    @NotNull(message = "sort column must not be null")
    String sortColumn,

    @NotBlank(message = "sort order must not be blank")
    @NotNull(message = "sort order cannot be null")
    String sortOrder,

    @NotNull(message = "search term cannot be null")
    String searchTerm,

    @NotNull(message = "min created date cannot be null")
    LocalDateTime minCreatedAt,

    @NotNull(message = "max created date cannot be null")
    LocalDateTime maxCreatedAt,

    @NotNull(message = "min copies cannot be null")
    Integer minCopies,

    @NotNull(message = "max copies cannot be null")
    Integer maxCopies,

    @NotNull(message = "min ratings cannot be null")
    Float minRatings,

    @NotNull(message = "max ratings cannot be null")
    Float maxRatings
) {
}
