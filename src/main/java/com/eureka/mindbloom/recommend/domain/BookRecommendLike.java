package com.eureka.mindbloom.recommend.domain;

import com.eureka.mindbloom.common.domain.BaseEntity;
import com.eureka.mindbloom.member.domain.Child;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookRecommendLike extends BaseEntity {

	@Id
	@Column(name = "book_recommend_id")
	private Long bookRecommendId;

	@MapsId
	@OneToOne
	private BookRecommend bookRecommend;

	@ManyToOne
	@JoinColumn(name = "child_id")
	private Child child;

	private String traitValue;

	public BookRecommendLike(BookRecommend bookRecommend, Child child, String traitValue) {
		this.bookRecommend = bookRecommend;
		this.child = child;
		this.traitValue = traitValue;
	}
}
