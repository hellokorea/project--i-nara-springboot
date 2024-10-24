package com.eureka.mindbloom.book.repository.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.Instant;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eureka.mindbloom.recommend.dto.ChildBooks;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookRecommendBulkRepository {
	private final JdbcTemplate jdbcTemplate;

	@Transactional
	public void saveAll(ChildBooks childBooks) {
		String sql = "INSERT INTO Book_Recommend (book_id, child_id,created_at) " +
			"VALUES (?, ?, ?)";
		Long childId = childBooks.getChildId();
		Date date = new Date(Instant.now().toEpochMilli());
		jdbcTemplate.batchUpdate(sql,
			childBooks.getTraitBooksIsbn(),
			childBooks.getTraitBooksIsbn().size(),
			(PreparedStatement ps, String traitBook) -> {
				ps.setString(1, traitBook);
				ps.setLong(2, childId);
				ps.setDate(3, date);
			});
	}
}
