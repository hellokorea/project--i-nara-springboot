package com.eureka.mindbloom.book.domain;

import com.eureka.mindbloom.common.domain.BaseEntity;
import com.eureka.mindbloom.member.domain.Child;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookLike extends BaseEntity {

	@EmbeddedId
	private BookChildId bookChildId;

	private String type;

	@MapsId("bookId")
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@MapsId("childId")
	@ManyToOne
	@JoinColumn(name = "child_id")
	private Child child;

	@Builder
	public BookLike(String type, Book book, Child child) {
		this.bookChildId = new BookChildId(book.getIsbn(), child.getId());
		this.type = type;
		this.book = book;
		this.child = child;
	}
}
