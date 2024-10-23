package com.eureka.mindbloom.book.controller;

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

        bookLikeService.addLike(bookId, request.getChildId(), request.getType());

        BookLikeResponse response = BookLikeResponse.builder()
                .bookId(bookId)
                .childId(request.getChildId())
                .type(request.getType())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("도서 좋아요 상태가 성공적으로 변경되었습니다.", response));
    }
}
