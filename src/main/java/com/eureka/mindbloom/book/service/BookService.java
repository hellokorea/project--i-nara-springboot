package com.eureka.mindbloom.book.service;

import com.eureka.mindbloom.book.dto.BookDetailResponse;
import com.eureka.mindbloom.book.dto.ReadBookResponse;
import com.eureka.mindbloom.book.dto.BookListResponse;
import com.eureka.mindbloom.book.dto.SimilarBookResponse;
import com.eureka.mindbloom.book.type.SortOption;
import com.eureka.mindbloom.member.domain.Member;

import java.util.List;

public interface BookService {
    BookListResponse getBooks(String categoryCode, String search, int page, SortOption sortOption);

    BookListResponse getRecentlyViewedBooks(int page, Long childId, Member member);

    BookDetailResponse getBookDetail(String isbn);

    ReadBookResponse readBook(String isbn, Long childId, Member member);

    List<SimilarBookResponse> getBooksByCategory(String isbn, int limit);
}
