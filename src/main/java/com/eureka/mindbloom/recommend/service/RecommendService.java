package com.eureka.mindbloom.recommend.service;

import java.util.List;

import com.eureka.mindbloom.book.dto.BookRecommendResponse;

public interface RecommendService {
	List<BookRecommendResponse> getRecommendBooks(Long userId);
}
