package com.eureka.mindbloom.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.mindbloom.book.domain.BookCategory;
import com.eureka.mindbloom.book.domain.BookCategoryId;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId> {
}
