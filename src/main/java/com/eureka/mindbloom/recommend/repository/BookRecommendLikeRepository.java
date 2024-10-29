package com.eureka.mindbloom.recommend.repository;

import java.util.List;

import com.eureka.mindbloom.recommend.domain.BookRecommendLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRecommendLikeRepository extends JpaRepository<BookRecommendLike, Long> {
	@Query("SELECT br.book.isbn FROM BookRecommendLike brl LEFT JOIN BookRecommend br ON brl.bookRecommendId = br.id WHERE brl.traitValue = :trait")
	List<String> findIsbnByTraitValue(String trait);
}
