package com.eureka.mindbloom.book.service;

import com.eureka.mindbloom.book.domain.BookLike;
import com.eureka.mindbloom.book.dto.BookLikeStatsResponse;

import java.util.List;

public interface BookLikeService {
    BookLike addLike(String isbn, Long childId, String type);
    void removeLike(String isbn, Long childId);
    List<BookLikeStatsResponse> getBookLikeStats(String isbn);

}
