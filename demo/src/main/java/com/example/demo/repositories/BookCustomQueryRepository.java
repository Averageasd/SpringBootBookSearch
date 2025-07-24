package com.example.demo.repositories;

import com.example.demo.dtos.BookPaginationSearchDTO;
import com.example.demo.dtos.BookResponseDTO;
import com.example.demo.entities.BookEntity;

import java.util.List;

public interface BookCustomQueryRepository {
    public List<BookResponseDTO> getPaginatedBooks(BookPaginationSearchDTO bookPaginationSearchDTO);
    public boolean bookWithNameExist(String title);
}
