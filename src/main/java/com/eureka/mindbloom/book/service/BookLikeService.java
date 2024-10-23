package com.eureka.mindbloom.book.service;

import com.eureka.mindbloom.book.domain.BookLike;

public interface BookLikeService {
    BookLike addLike(String isbn, Long childId, String type);
    void removeLike(String isbn, Long childId);
}
