package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.BookLikeStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

public interface BookLikeStatsRepository extends JpaRepository<BookLikeStats, Long> {

    @Query("SELECT bls.count FROM BookLikeStats bls WHERE bls.book.isbn = :isbn")
    Long findLikeCountByIsbn(@Param("isbn") String isbn);
}
