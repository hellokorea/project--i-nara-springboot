package com.eureka.mindbloom.book.controller;

import com.eureka.mindbloom.book.domain.BookLike;
import com.eureka.mindbloom.book.dto.BookLikeRequest;
import com.eureka.mindbloom.book.dto.BookLikeResponse;
import com.eureka.mindbloom.book.service.BookLikeService;
import com.eureka.mindbloom.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookLikeController {

    private final BookLikeService bookLikeService;

    @PutMapping("/{bookId}/likes")
    public ResponseEntity<ApiResponse<BookLikeResponse>> updateLike(
        @PathVariable String bookId,
        @RequestBody BookLikeRequest request) {

        BookLike bookLike = bookLikeService.addLike(bookId, request.getChildId(), request.getType());

        BookLikeResponse response = BookLikeResponse.builder()
                .bookId(bookLike.getBook().getIsbn())
                .childId(bookLike.getChild().getId())
                .type(bookLike.getType())
                .likedAt(bookLike.getCreatedAt().toString())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("도서 좋아요가 등록되었습니다.", response));
    }

    @DeleteMapping("/{bookId}/likes")
    public ResponseEntity<ApiResponse<Void>> removeLike(
            @PathVariable String bookId,
            @RequestBody BookLikeRequest request) {

        bookLikeService.removeLike(bookId, request.getChildId());

        return ResponseEntity.ok(ApiResponse.success("도서 좋아요가 취소되었습니다."));
    }
}
