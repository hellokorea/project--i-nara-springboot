package com.eureka.mindbloom.recommend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eureka.mindbloom.recommend.domain.BookRecommend;
import com.eureka.mindbloom.book.dto.BookRecommendResponse;

public interface BookRecommendRepository extends JpaRepository<BookRecommend, Long> {
	@Query("SELECT new com.eureka.mindbloom.book.dto.BookRecommendResponse(b.isbn, b.title, b.author, b.coverImage, "
		+ "CASE WHEN brl.bookRecommend.id IS NOT NULL THEN true ELSE false END , br.id) "
		+ "FROM BookRecommend br "
		+ "JOIN Book b ON br.book.isbn = b.isbn "
		+ "left JOIN BookRecommendLike brl ON br.id = brl.bookRecommend.id "
		+ "where br.child.id = :childId AND DATE(br.createdAt) = current_date")
	List<BookRecommendResponse> findByChildIdAndCurrentDate(Long childId);

	@Query("SELECT br.book.isbn FROM BookRecommend br LEFT JOIN BookView bv ON br.book.isbn = bv.book.isbn WHERE br.child.id = :childId AND bv.id IS NULL")
	List<String> findByNotReadRecommendBooks(Long childId);

}
