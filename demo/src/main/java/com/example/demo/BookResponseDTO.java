package com.example.demo;


import java.time.LocalDateTime;
import java.util.UUID;

public record BookResponseDTO(
        UUID id,
        String title,
        LocalDateTime createdAt,
        String description,
        int copies,
        double rating,
        String author
        ) {}