package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookResponseDTO> getSingleBook(@PathVariable UUID id){
        try{
            if (!this.bookService.bookExist(id)){
                throw new Exception();
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(this.bookService.getBook(id));
        }
        catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }


    }
    @GetMapping(path = "/all-books")
    public List<BookEntity> getBooks(){
        return this.bookService.GetBooks();
    }

    @PostMapping(path = "/new-book")
    public ResponseEntity<String> createBook(@RequestBody BookCreateRequestDTO bookCreateRequestDTO){
        try{
            BookEntity bookEntity = new BookEntity(
                    bookCreateRequestDTO.getTitle(),
                    bookCreateRequestDTO.getDescription(),
                    bookCreateRequestDTO.getCopies(),
                    bookCreateRequestDTO.getRating(),
                    bookCreateRequestDTO.getAuthor()
            );

            this.bookService.insertNewBook(bookEntity);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (Exception exception){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable UUID id){
        try{
            if (!bookService.bookExist(id)){
                throw new Exception();
            }
            this.bookService.deleteBook(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (Exception exception){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
