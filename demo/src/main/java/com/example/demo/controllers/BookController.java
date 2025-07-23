package com.example.demo.controllers;

import com.example.demo.dtos.BookCreateRequestDTO;
import com.example.demo.dtos.BookPaginationSearchDTO;
import com.example.demo.dtos.BookResponseDTO;
import com.example.demo.dtos.BookUpdateRequestDTO;
import com.example.demo.entities.BookEntity;
import com.example.demo.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookResponseDTO> getSingleBook(@PathVariable UUID id) {
        try {
            BookResponseDTO bookResponseDTO = this.bookService.getBook(id);
            if (bookResponseDTO == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bookResponseDTO);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @GetMapping(path = "/all-books")
    public ResponseEntity<?> getBooks(
            @Valid @ModelAttribute BookPaginationSearchDTO bookPaginationSearchDTO,
            BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors().stream()
                        .map(e -> e.getField() + ": " + e.getDefaultMessage())
                        .toList();
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(errors);
            }

            return ResponseEntity.ok(bookService.getPaginatedBooks(bookPaginationSearchDTO));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

    }

    @PostMapping(path = "/new-book")
    public ResponseEntity<String> createBook(@RequestBody BookCreateRequestDTO bookCreateRequestDTO) {
        try {
            BookEntity bookEntity = new BookEntity(
                    bookCreateRequestDTO.title(),
                    bookCreateRequestDTO.description(),
                    bookCreateRequestDTO.copies(),
                    bookCreateRequestDTO.rating(),
                    bookCreateRequestDTO.author()
            );

            this.bookService.insertNewBook(bookEntity);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Void> patchBook(@PathVariable UUID id, @RequestBody BookUpdateRequestDTO bookUpdateRequestDTO) {
        try {
            if (!bookService.patchBook(id, bookUpdateRequestDTO)){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        try {
            if (!this.bookService.deleteBook(id)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
