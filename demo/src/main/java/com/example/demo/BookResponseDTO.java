package com.example.demo;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BookResponseDTO(
        UUID id,
        String title,
        OffsetDateTime createdAt,
        String description,
        int copies,
        double rating,
        String author
        ) {}