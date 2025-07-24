package com.example.demo;

import com.example.demo.exceptions.BookAlreadyExistException;
import com.example.demo.exceptions.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({BookNotFoundException.class})
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({BookAlreadyExistException.class})
    public ResponseEntity<Object> handleBookAlreadyExistsException(BookAlreadyExistException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleInternalErrorException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

}
