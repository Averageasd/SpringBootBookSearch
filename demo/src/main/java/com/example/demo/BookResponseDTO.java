package com.example.demo;

import java.util.UUID;

public class BookResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private int copies;
    private double rating;
    private String author;
    public BookResponseDTO(){}
    public BookResponseDTO(UUID id, String title, String description, int copies, double rating, String author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.copies = copies;
        this.rating = rating;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
