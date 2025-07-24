package com.example.demo.services;

import com.example.demo.dtos.BookCreateRequestDTO;
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
        List<BookEntity> bookEntities = bookRepository.getPaginatedBooks(bookPaginationSearchDTO);
        List<BookResponseDTO> bookResponseDTOS = new ArrayList<>();
        for (BookEntity bookEntity : bookEntities) {
            bookResponseDTOS.add(
                    new BookResponseDTO(
                            bookEntity.getId(),
                            bookEntity.getTitle(),
                            bookEntity.getCreatedAt(),
                            bookEntity.getDescription(),
                            bookEntity.getCopies(),
                            bookEntity.getRating(),
                            bookEntity.getAuthor()));
        }
        return bookResponseDTOS;
    }

    @Transactional
    public void insertNewBook(BookCreateRequestDTO bookCreateRequestDTO) {
        boolean bookExists = bookRepository.bookWithNameExist(bookCreateRequestDTO.title());
        if (bookExists) {
            throw new BookAlreadyExistException("book with name or id already exists");
        } else {
            BookEntity bookEntity = new BookEntity(
                    bookCreateRequestDTO.title(),
                    bookCreateRequestDTO.description(),
                    bookCreateRequestDTO.copies(),
                    bookCreateRequestDTO.rating(),
                    bookCreateRequestDTO.author()
            );
            this.bookRepository.save(bookEntity);
        }

    }

    @Transactional
    public void deleteBook(UUID bookId) {
        BookEntity existingBook = getExistingBook(bookId);
        bookRepository.delete(existingBook);
    }

    private BookEntity getExistingBook(UUID id) {
        return this.bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("book not found with existing id"));
    }

    public BookResponseDTO getBook(UUID id) {
        BookEntity singleBookEntity = getExistingBook(id);
        return new BookResponseDTO(singleBookEntity.getId(), singleBookEntity.getTitle(), singleBookEntity.getCreatedAt(), singleBookEntity.getDescription(), singleBookEntity.getCopies(), singleBookEntity.getRating(), singleBookEntity.getAuthor());
    }

    @Transactional
    public void patchBook(UUID id, BookUpdateRequestDTO bookUpdateRequestDTO) {
        BookEntity bookEntity = getExistingBook(id);
        updateBookFields(bookEntity, bookUpdateRequestDTO);
    }

    private void updateBookFields(BookEntity bookEntity, BookUpdateRequestDTO bookUpdateRequestDTO) {
        bookEntity.setTitle(bookUpdateRequestDTO.title());
        bookEntity.setDescription(bookUpdateRequestDTO.description());
        bookRepository.save(bookEntity);
    }

}
