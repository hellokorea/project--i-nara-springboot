package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.Book;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eureka.mindbloom.book.domain.BookCategory;
import com.eureka.mindbloom.book.domain.BookCategoryId;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId> {

	@Query("SELECT b.id.isbn FROM BookCategory b WHERE b.id.categoryTraitId.categoryCode = :categoryCode")
	List<String> findIsbnByCategoryCodes(String categoryCode);

	@Query("SELECT bc.id.categoryTraitId.categoryCode FROM BookCategory bc WHERE bc.id.isbn = :isbn")
	String findCategoryCodeByIsbn(@Param("isbn") String isbn);

	@Query("SELECT b.id.isbn FROM BookCategory b WHERE b.id.categoryTraitId.traitCode = :traitCodes")
	List<String> findIsbnByTraitCode(String traitCodes);

	void deleteByBook(Book book);

	void deleteByBookIsbn(String isbn);

}

