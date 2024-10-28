package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.Book;
import com.eureka.mindbloom.book.domain.BookView;
import com.eureka.mindbloom.member.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookViewRepository extends JpaRepository<BookView, Long> {
    Optional<BookView> findTopByBookAndChildOrderByCreatedAtDesc(Book book, Child child);
}
