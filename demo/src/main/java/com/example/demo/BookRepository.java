package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, UUID> {


    @Query(value = "SELECT " +
            "search_books" +
            "(:page, " +
            ":sort_column, " +
            ":sort_order, " +
            ":search_term, " +
            ":min_created_at, " +
            ":max_created_at, " +
            ":min_copies, " +
            ":max_copies, " +
            ":min_rating, " +
            ":max_rating" +
            ")",
            nativeQuery = true)
    public List<BookEntity> getPaginatedBooks(
            @Param("page") Integer page,
            @Param("sort_column") String sortColumn,
            @Param("sort_order") String sortOrder,
            @Param("search_term") String searchTerm,
            @Param("min_created_at") Timestamp minCreatedAt,
            @Param("max_created_at") Timestamp maxCreatedAt,
            @Param("min_copies") Integer minCopies,
            @Param("max_copies") Integer maxCopies,
            @Param("min_rating") Float minRating,
            @Param("max_rating") Float maxRating
    );
}
