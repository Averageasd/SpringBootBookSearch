package com.example.demo.services;

import com.example.demo.dtos.BookPaginationSearchDTO;
import com.example.demo.dtos.BookResponseDTO;
import com.example.demo.dtos.BookUpdateRequestDTO;
import com.example.demo.entities.BookEntity;
import com.example.demo.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponseDTO> getPaginatedBooks(BookPaginationSearchDTO bookPaginationSearchDTO) {
        return bookRepository.getPaginatedBooks(bookPaginationSearchDTO);
    }

    @Transactional
    public void insertNewBook(BookEntity bookEntity) {
        this.bookRepository.save(bookEntity);
    }

    @Transactional
    public boolean deleteBook(UUID bookId) {
        if (bookExist(bookId)){
            this.bookRepository.deleteById(bookId);
            return true;
        }
        return false;
    }

    public boolean bookExist(UUID id) {
        return this.bookRepository.existsById((id));
    }

    public BookResponseDTO getBook(UUID id) {
        Optional<BookEntity> singleBookOptional = this.bookRepository.findById(id);
        BookEntity singleBookEntity = singleBookOptional.get();
        return new BookResponseDTO(
                singleBookEntity.getId(),
                singleBookEntity.getTitle(),
                singleBookEntity.getCreatedAt(),
                singleBookEntity.getDescription(),
                singleBookEntity.getCopies(),
                singleBookEntity.getRating(),
                singleBookEntity.getAuthor()
        );
    }

    @Transactional
    public boolean patchBook(UUID id, BookUpdateRequestDTO bookUpdateRequestDTO) {
        Optional<BookEntity> bookEntityOptional = bookRepository.findById(id);
        return bookEntityOptional.filter(bookEntity -> updateBookFields(bookEntity, bookUpdateRequestDTO)).isPresent();
    }

    private boolean updateBookFields(BookEntity bookEntity, BookUpdateRequestDTO bookUpdateRequestDTO){
        bookEntity.setTitle(bookUpdateRequestDTO.title());
        bookEntity.setDescription(bookUpdateRequestDTO.description());
        bookRepository.save(bookEntity);
        return true;
    }

}
