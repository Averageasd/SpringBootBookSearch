package com.example.demo.repositories;

import com.example.demo.dtos.BookPaginationSearchDTO;
import com.example.demo.dtos.BookResponseDTO;

import java.util.List;

public interface BookCustomQueryRepository {
    public List<BookResponseDTO> getPaginatedBooks(BookPaginationSearchDTO bookPaginationSearchDTO);
}
