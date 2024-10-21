package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.BookRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRecommendRepository extends JpaRepository<BookRecommend, Long> {
}
