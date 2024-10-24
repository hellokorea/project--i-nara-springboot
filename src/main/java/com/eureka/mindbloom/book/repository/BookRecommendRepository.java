package com.eureka.mindbloom.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eureka.mindbloom.book.domain.BookRecommend;
import com.eureka.mindbloom.book.dto.BookRecommendResponse;

public interface BookRecommendRepository extends JpaRepository<BookRecommend, Long> {
	@Query("SELECT new com.eureka.mindbloom.book.dto.BookRecommendResponse(b.isbn, b.title, b.author, b.coverImage, "
		+ "CASE WHEN brl.id IS NOT NULL THEN true ELSE false END )"
		+ "FROM BookRecommend br "
		+ "JOIN Book b ON br.book.isbn = b.isbn "
		+ "left JOIN BookRecommendLike brl ON br.id = brl.id.bookRecommendId "
		+ "where br.child.id = :childId AND br.createdAt = current_date")
	List<BookRecommendResponse> findByChildIdAndCurrentDate(Long childId);

	@Query("SELECT br.book.isbn FROM BookRecommend br where br.child.id = :childId And br.book.isbn Not In (SELECT distinct bv.book.isbn FROM BookView bv WHERE bv.child.id = :childId)")
	List<String> findByNotReadRecommendBooks(Long childId);

}
