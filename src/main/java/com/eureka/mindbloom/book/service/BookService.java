package com.eureka.mindbloom.book.service;

import com.eureka.mindbloom.book.dto.BooksResponse;
import org.springframework.data.domain.Slice;

public interface BookService {
    Slice<BooksResponse> getBooks(String categoryCode, String search, int page, SortOption sortOption);
}
