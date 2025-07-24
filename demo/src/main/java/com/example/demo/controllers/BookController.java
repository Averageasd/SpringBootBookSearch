package com.example.demo.controllers;

import com.example.demo.dtos.BookCreateRequestDTO;
import com.example.demo.dtos.BookPaginationSearchDTO;
import com.example.demo.dtos.BookResponseDTO;
import com.example.demo.dtos.BookUpdateRequestDTO;
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
        BookResponseDTO bookResponseDTO = this.bookService.getBook(id);
        return ResponseEntity.ok(bookResponseDTO);
    }

    @GetMapping(path = "/all-books")
    public ResponseEntity<?> getBooks(
            @Valid @ModelAttribute BookPaginationSearchDTO bookPaginationSearchDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }
        return ResponseEntity.ok(bookService.getPaginatedBooks(bookPaginationSearchDTO));

    }

    @PostMapping(path = "/new-book")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookCreateRequestDTO bookCreateRequestDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }
        bookService.insertNewBook(bookCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> patchBook(@PathVariable UUID id,
                                       @Valid @RequestBody BookUpdateRequestDTO bookUpdateRequestDTO,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }
        bookService.patchBook(id, bookUpdateRequestDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
