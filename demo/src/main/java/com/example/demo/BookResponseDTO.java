package com.example.demo;

import jakarta.persistence.Column;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

public record BookResponseDTO(
        UUID id,
        String title,
        Timestamp createdAt,
        String description,
        int copies,
        double rating,
        String author
        ) {}