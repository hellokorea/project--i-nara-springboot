package com.eureka.mindbloom.book.dto;

import lombok.Getter;

@Getter
public class BookRecommendResponse {
	private String isbn;
	private String title;
	private String author;
	private String coverImage;
	private boolean recommendLike;

	public BookRecommendResponse(String isbn, String title, String author, String coverImage, boolean recommendLike) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.coverImage = coverImage;
		this.recommendLike = recommendLike;
	}

}
