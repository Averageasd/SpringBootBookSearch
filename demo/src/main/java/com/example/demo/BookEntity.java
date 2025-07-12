package com.example.demo;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "book")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid DEFAULT uuid_generate_v1()", insertable = false, updatable = false, nullable = false)
    private UUID id;

    @Column(length = 50, nullable = false, unique = true)
    private String title;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(length = 250, nullable = false)
    private String description;

    @Column(nullable = false)
    private int copies;

    @Column(nullable = false)
    private double rating;

    @Column(length = 50, nullable = false)
    private String author;

    public BookEntity(){}

    public BookEntity(
            String title,
            String description,
            int copies,
            double rating,
            String author) {

        this.title = title;
        this.description = description;
        this.copies = copies;
        this.rating = rating;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
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

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
