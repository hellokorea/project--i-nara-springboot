package com.eureka.mindbloom.recommend.repository;

import com.eureka.mindbloom.recommend.domain.BookRecommendLike;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRecommendLikeRepository extends JpaRepository<BookRecommendLike, Long> {
}
