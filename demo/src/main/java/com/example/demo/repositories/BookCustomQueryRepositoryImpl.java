package com.example.demo.repositories;

import com.example.demo.dtos.BookPaginationSearchDTO;
import com.example.demo.dtos.BookResponseDTO;
import com.example.demo.entities.BookEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class BookCustomQueryRepositoryImpl implements BookCustomQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BookCustomQueryRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BookResponseDTO> getPaginatedBooks(BookPaginationSearchDTO bookPaginationSearchDTO) {
        String sql = "SELECT * FROM" +
                " search_books" +
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
                ")";
        List<BookResponseDTO> bookResponseDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("page", bookPaginationSearchDTO.page());
        params.put("sort_column", bookPaginationSearchDTO.sortColumn());
        params.put("sort_order", bookPaginationSearchDTO.sortOrder());
        params.put("search_term", bookPaginationSearchDTO.searchTerm());
        params.put("min_created_at", bookPaginationSearchDTO.minCreatedAt());
        params.put("max_created_at", bookPaginationSearchDTO.maxCreatedAt());
        params.put("min_copies", bookPaginationSearchDTO.minCopies());
        params.put("max_copies", bookPaginationSearchDTO.maxCopies());
        params.put("min_rating", bookPaginationSearchDTO.minRatings());
        params.put("max_rating", bookPaginationSearchDTO.maxRatings());
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
        for (Map<String, Object> row : rows) {
            System.out.println(row.keySet());
            UUID id = (UUID) row.get("id");
            String title = (String) row.get("title");
            String description = (String) row.get("description");
            String author = (String) row.get("author");
            int copies = ((Number) row.get("copies")).intValue();
            double rating = ((Number) row.get("rating")).doubleValue();
            Timestamp createdAt = ((Timestamp) row.get("created_at"));
            LocalDateTime localCreatedAt = createdAt.toLocalDateTime();
            bookResponseDTOS.add(new BookResponseDTO(id, title, localCreatedAt, description, copies, rating, author));
        }
        return bookResponseDTOS;
    }

    @Override
    public boolean bookWithNameExist(String titleParam) {
        String sql = "SELECT TOP 1 * FROM book b WHERE b.title = :title";
        Map<String, Object> params = new HashMap<>();
        params.put("title", titleParam);
        RowMapper<BookEntity> bookEntityRowMapper = (row, rowNumber) -> {
            UUID id = (UUID) row.getObject("id");
            String title = (String) row.getObject("title");
            String description = (String) row.getObject("description");
            String author = (String) row.getObject("author");
            int copies = ((Number) row.getObject("copies")).intValue();
            double rating = ((Number) row.getObject("rating")).doubleValue();
            Timestamp createdAt = ((Timestamp) row.getObject("created_at"));
            LocalDateTime localCreatedAt = createdAt.toLocalDateTime();
            BookEntity bookEntity = new BookEntity(title, description, copies, rating, author);
            bookEntity.setId(id);
            bookEntity.setCreatedAt(localCreatedAt);
            return bookEntity;
        };
        BookEntity bookEntity = jdbcTemplate.queryForObject(
                sql,
                params,
                bookEntityRowMapper);
        if (bookEntity == null){
            return false;
        }
        return bookEntity.getTitle().equals(titleParam);
    }
}
