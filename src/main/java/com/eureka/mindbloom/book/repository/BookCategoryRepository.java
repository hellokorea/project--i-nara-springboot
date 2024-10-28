package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.Book;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eureka.mindbloom.book.domain.BookCategory;
import com.eureka.mindbloom.book.domain.BookCategoryId;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId> {

	@Query("SELECT b.id.isbn FROM BookCategory b WHERE b.id.categoryTraitId.categoryCode = :categoryCodes")
	List<String> findIsbnByCategoryCodes(String categoryCodes);

	@Query("SELECT b.id.isbn FROM BookCategory b WHERE b.id.categoryTraitId.categoryCode IN :categoryCodes")
	List<String> findIsbnByCategoryTrait_Id_CategoryCodeIn(List<String> categoryCodes);

	@Query("SELECT b.id.isbn FROM BookCategory b WHERE b.id.categoryTraitId.traitCode IN :traitCodes AND b.id.isbn IN :preferredBooks")
	List<String> findIsbnByTraitCodesAndPreferredBooksIn(String[] traitCodes, List<String> preferredBooks);

	@Query("SELECT b.id.isbn FROM BookCategory b WHERE b.id.categoryTraitId.traitCode IN :traitCodes")
	List<String> findIsbnByTraitCodes(String[] traitCodes);

	@Query("SELECT b.id.isbn FROM BookCategory b WHERE b.id.categoryTraitId.traitCode = :traitCodes")
	List<String> findIsbnByTraitCode(String traitCodes);

    void deleteByBook(Book book);

    void deleteByBookIsbn(String isbn);

}
