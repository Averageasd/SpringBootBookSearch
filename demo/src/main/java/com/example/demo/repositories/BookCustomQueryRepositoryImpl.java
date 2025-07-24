package com.example.demo.repositories;

import com.example.demo.customMappers.BookEntityMapper;
import com.example.demo.dtos.BookPaginationSearchDTO;
import com.example.demo.dtos.BookResponseDTO;
import com.example.demo.entities.BookEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class BookCustomQueryRepositoryImpl implements BookCustomQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BookCustomQueryRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate)    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BookEntity> getPaginatedBooks(BookPaginationSearchDTO bookPaginationSearchDTO) {
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
        return jdbcTemplate.query(sql, params, new BookEntityMapper());
    }

    @Override
    public boolean bookWithNameExist(String titleParam) {
        String sql = "SELECT TOP 1 * FROM book b WHERE b.title = :title";
        Map<String, Object> params = new HashMap<>();
        params.put("title", titleParam);
        List<BookEntity> bookEntities = jdbcTemplate.query(
                sql,
                params,
                new BookEntityMapper());
        return !bookEntities.isEmpty() && bookEntities.getFirst().getTitle().equals(titleParam);
    }
}
