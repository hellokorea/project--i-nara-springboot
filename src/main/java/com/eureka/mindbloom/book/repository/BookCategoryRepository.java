package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.Book;
import com.eureka.mindbloom.book.domain.BookCategory;
import com.eureka.mindbloom.book.domain.BookCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId> {

	@Query("SELECT b.id.isbn FROM BookCategory b WHERE b.id.categoryTraitId.categoryCode = :categoryCode")
	List<String> findIsbnByCategoryCodes(String categoryCode);

	@Query("SELECT bc FROM BookCategory bc WHERE bc.id.isbn = :isbn")
	BookCategory findByIsbn(@Param("isbn") String isbn);

	@Query("SELECT b.id.isbn FROM BookCategory b WHERE b.id.categoryTraitId.traitCode = :traitCodes")
	List<String> findIsbnByTraitCode(String traitCodes);

	void deleteByBook(Book book);

	// 명시적인 JPQL 쿼리를 사용하여 특정 isbn을 기준으로 BookCategory 레코드 삭제
	@Modifying
	@Transactional
	@Query("DELETE FROM BookCategory bc WHERE bc.book.isbn = :isbn")
	void deleteByBookIsbn(@Param("isbn") String isbn);
}
