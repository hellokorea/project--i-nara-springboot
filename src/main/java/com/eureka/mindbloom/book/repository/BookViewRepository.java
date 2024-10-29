package com.eureka.mindbloom.book.repository;

import java.util.List;
import java.util.Optional;

import com.eureka.mindbloom.book.domain.Book;
import com.eureka.mindbloom.book.domain.BookView;
import com.eureka.mindbloom.member.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BookViewRepository extends JpaRepository<BookView, Long> {
    @Query("SELECT distinct bv.book.isbn FROM BookView bv WHERE bv.child.id = :childId")
    List<String> findReadIsbnByChildId(Long childId);

    boolean existsByBookAndChild(Book book, Child child);
}
