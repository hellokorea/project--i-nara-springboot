package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.recommend.domain.BookRecommendLike;
import com.eureka.mindbloom.book.domain.RecommendLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRecommendLikeRepository extends JpaRepository<BookRecommendLike, RecommendLikeId> {
}
