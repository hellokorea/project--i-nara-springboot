package com.eureka.mindbloom.recommend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eureka.mindbloom.book.dto.BookRecommendResponse;
import com.eureka.mindbloom.book.repository.BookRecommendRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
	private final BookRecommendRepository bookRecommendRepository;

	@Override
	public List<BookRecommendResponse> getRecommendBooks(Long userId) {
		List<BookRecommendResponse> books = bookRecommendRepository.findByChildIdAndCurrentDate(userId);
		return books;
	}
}
