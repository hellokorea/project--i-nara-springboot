package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

}
