package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.Book;
import com.eureka.mindbloom.book.domain.BookCategory;
import com.eureka.mindbloom.book.domain.BookCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId> {
    void deleteByBook(Book book);
    void deleteByBookIsbn(String isbn);
}
