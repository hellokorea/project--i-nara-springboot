package com.eureka.mindbloom.book.repository;

import java.util.List;

import com.eureka.mindbloom.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, String> {

	@Query("SELECT b.isbn FROM Book b JOIN BookCategory bc ON b.isbn = bc.id.isbn WHERE bc.id.categoryTraitId.categoryCode IN :preferences ORDER BY b.createdAt DESC LIMIT 2")
	List<String> findIsbnByCategoryCodeSortRecent(List<String> preferences);

	@Query("SELECT b.isbn FROM Book b WHERE b.isbn IN :isbns AND b.isbn NOT IN (SELECT distinct bv.book.isbn FROM BookView bv WHERE bv.child.id = :childId)")
	List<String> findNotReadIsbnByIsbnAndBookView(Long childId, List<String> isbns);

	@Query("SELECT b.isbn FROM Book b WHERE b.isbn IN :isbns AND b.isbn NOT IN :notReadRecommendBooks")
	List<String> findIsbnByIsbnAndNotReadRecommendBooks(List<String> isbns, List<String> notReadRecommendBooks);

	@Query("SELECT b.isbn FROM Book b WHERE b.isbn IN :books ORDER BY b.viewCount DESC LIMIT 10")
	List<String> findIsbnByBooksInOrderByViewCount(List<String> books);

	@Query("SELECT b.isbn FROM Book b ORDER BY b.viewCount DESC LIMIT 10")
	List<String> findIsbnByOrderByViewCount();


}
