package com.eureka.mindbloom.recommend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eureka.mindbloom.book.dto.BookRecommendResponse;
import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.recommend.dto.RecommendBookLikeRequest;
import com.eureka.mindbloom.recommend.service.RecommendService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member/books/recommend")
@RequiredArgsConstructor
public class RecommendController {

	private final RecommendService recommendService;

	@GetMapping("/{childId}")
	public ResponseEntity<ApiResponse<List<BookRecommendResponse>>> getRecommendBooks(@PathVariable Long childId) {
		List<BookRecommendResponse> responses = recommendService.getRecommendBooks(childId);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiResponse.success("성공적으로 추천 도서 목록을 조회했습니다.", responses));
	}
	@PutMapping("/like/{childId}")
	public ResponseEntity<ApiResponse<?>> likeRecommendBook(@PathVariable Long childId , @Valid @RequestBody RecommendBookLikeRequest request) {
		recommendService.likeRecommendBook(childId, request.bookRecommendId(), request.likeType());
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiResponse.success("성공적으로 추천 좋아요를 반영했습니다.", null));
	}
}
