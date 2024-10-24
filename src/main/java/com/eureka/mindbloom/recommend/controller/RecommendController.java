package com.eureka.mindbloom.recommend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eureka.mindbloom.book.dto.BookRecommendResponse;
import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.recommend.service.RecommendService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/books/recommend")
@RequiredArgsConstructor
public class RecommendController {

	private final RecommendService recommendService;

	@GetMapping("/{childId}")
	public ResponseEntity<ApiResponse<List<BookRecommendResponse>>> getRecommendBooks(@PathVariable Long childId) {
		List<BookRecommendResponse> responses = recommendService.getRecommendBooks(childId);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiResponse.success("오늘의 추천 도서 목록을 성공적으로 조회했습니다.", responses));
	}
}
