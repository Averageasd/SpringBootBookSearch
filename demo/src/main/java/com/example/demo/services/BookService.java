package com.example.demo.services;

import com.example.demo.dtos.BookPaginationSearchDTO;
import com.example.demo.dtos.BookResponseDTO;
import com.example.demo.dtos.BookUpdateRequestDTO;
import com.example.demo.entities.BookEntity;
import com.example.demo.exceptions.BookAlreadyExistException;
import com.example.demo.exceptions.BookNotFoundException;
import com.example.demo.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        boolean bookExists = bookRepository.existsById(bookEntity.getId()) || bookRepository.bookWithNameExist(bookEntity.getTitle());
        if (bookExists) {
            throw new BookAlreadyExistException("book with name or id already exists");
        } else {
            this.bookRepository.save(bookEntity);
        }

    }

    @Transactional
    public boolean deleteBook(UUID bookId) {
        BookEntity existingBook = getExistingBook(bookId);
        bookRepository.delete(existingBook);
        return true;
    }

    private BookEntity getExistingBook(UUID id) {
        return this.bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("book not found with existing id"));
    }

    public BookResponseDTO getBook(UUID id) {
        BookEntity singleBookEntity = getExistingBook(id);
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
        BookEntity bookEntity = getExistingBook(id);
        return updateBookFields(bookEntity, bookUpdateRequestDTO);
    }

    private boolean updateBookFields(BookEntity bookEntity, BookUpdateRequestDTO bookUpdateRequestDTO) {
        bookEntity.setTitle(bookUpdateRequestDTO.title());
        bookEntity.setDescription(bookUpdateRequestDTO.description());
        bookRepository.save(bookEntity);
        return true;
    }

}
