package com.example.demo.customMappers;

import com.example.demo.entities.BookEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class BookEntityMapper implements RowMapper<BookEntity> {
    @Override
    public BookEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String title = (String) rs.getObject("title");
        String description = (String) rs.getObject("description");
        String author = (String) rs.getObject("author");
        int copies = ((Number) rs.getObject("copies")).intValue();
        double rating = ((Number) rs.getObject("rating")).doubleValue();
        Timestamp createdAt = ((Timestamp) rs.getObject("created_at"));
        LocalDateTime localCreatedAt = createdAt.toLocalDateTime();
        BookEntity bookEntity = new BookEntity(title, description, copies, rating, author);
        bookEntity.setId(id);
        bookEntity.setCreatedAt(localCreatedAt);
        return bookEntity;
    }
}
