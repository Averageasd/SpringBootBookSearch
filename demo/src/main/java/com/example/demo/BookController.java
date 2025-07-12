package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookResponseDTO> getSingleBook(@PathVariable UUID id) {
        try {
            if (!this.bookService.bookExist(id)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(this.bookService.getBook(id));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }


    }

    @GetMapping(path = "/all-books")
    public List<BookEntity> getBooks() {
        return this.bookService.GetBooks();
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

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable UUID id) {
        try {
            if (!bookService.bookExist(id)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }
            this.bookService.deleteBook(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
