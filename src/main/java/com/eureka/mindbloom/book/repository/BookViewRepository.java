package com.eureka.mindbloom.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eureka.mindbloom.book.domain.Book;

public interface BookViewRepository extends JpaRepository<Book, Long> {
	@Query("SELECT distinct bv.book.isbn FROM BookView bv WHERE bv.child.id = :childId")
	List<String> findReadIsbnByChildId(Long childId);
}
