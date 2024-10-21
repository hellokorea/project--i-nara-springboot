package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.BookChildId;
import com.eureka.mindbloom.book.domain.BookLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLikeRepository extends JpaRepository<BookLike, BookChildId> {
}
