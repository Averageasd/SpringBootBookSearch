package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> GetBooks(){
        return this.bookRepository.findAll();
    }

    public void insertNewBook(BookEntity bookEntity){
        this.bookRepository.save(bookEntity);
    }

    public void deleteBook(UUID bookId) {
        this.bookRepository.deleteById(bookId);
    }

    public boolean bookExist(UUID id) {
        return this.bookRepository.existsById((id));
    }

    public BookResponseDTO getBook(UUID id){
        Optional<BookEntity> singleBookOptional = this.bookRepository.findById(id);
        BookEntity singleBookEntity = singleBookOptional.get();
        return new BookResponseDTO(
                singleBookEntity.getId(),
                singleBookEntity.getTitle(),
                singleBookEntity.getDescription(),
                singleBookEntity.getCopies(),
                singleBookEntity.getRating(),
                singleBookEntity.getAuthor()
                );
    }

}
