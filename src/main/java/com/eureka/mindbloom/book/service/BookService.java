package com.eureka.mindbloom.book.service;

import com.eureka.mindbloom.book.dto.BooksResponse;
import com.eureka.mindbloom.book.dto.RecentlyBookResponse;
import com.eureka.mindbloom.member.domain.Member;
import org.springframework.data.domain.Slice;

public interface BookService {
    Slice<BooksResponse> getBooks(String categoryCode, String search, int page, SortOption sortOption);

    RecentlyBookResponse getRecentlyViewedBooks(int page, Long childId, Member member);
}
