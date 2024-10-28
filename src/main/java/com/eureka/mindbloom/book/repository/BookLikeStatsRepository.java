package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.BookLikeStats;
import com.eureka.mindbloom.book.domain.BookLikeStatsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface BookLikeStatsRepository extends JpaRepository<BookLikeStats, BookLikeStatsId> {
    @Query("select stats from BookLikeStats stats "+
            "WHERE stats.id.isbn = :isbn")  // 수정된 부분
    List<BookLikeStats> likeCountByIsbn(@Param("isbn") String isbn);
}
