package com.eureka.mindbloom.recommend.domain;

import com.eureka.mindbloom.book.domain.RecommendLikeId;
import com.eureka.mindbloom.common.domain.BaseEntity;
import com.eureka.mindbloom.member.domain.Child;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookRecommendLike extends BaseEntity {

	@EmbeddedId
	private RecommendLikeId id;

	@MapsId("bookRecommendId")
	@ManyToOne
	@JoinColumn(name = "book_recommend_id")
	private BookRecommend bookRecommend;

	@MapsId("childId")
	@ManyToOne
	@JoinColumn(name = "child_id")
	private Child child;

	public BookRecommendLike(BookRecommend bookRecommend, Child child) {
		this.id = new RecommendLikeId(bookRecommend.getId(), child.getId());
		this.bookRecommend = bookRecommend;
		this.child = child;
	}
}
