package com.eureka.mindbloom.book.controller;

import com.eureka.mindbloom.book.dto.BookDetailResponse;
import com.eureka.mindbloom.book.dto.ReadBookResponse;
import com.eureka.mindbloom.book.dto.BookListResponse;
import com.eureka.mindbloom.book.dto.SimilarBookResponse;
import com.eureka.mindbloom.book.service.BookService;
import com.eureka.mindbloom.book.type.SortOption;
import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<BookListResponse>> getBooks(
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) SortOption sortOption
    ) {
        BookListResponse books = bookService.getBooks(categoryCode, search, page, sortOption);
        return ResponseEntity.ok(ApiResponse.success("OK", books));
    }

    @GetMapping("/similar")
    public ResponseEntity<ApiResponse<List<SimilarBookResponse>>> getSimilarBooks(
            @RequestParam String isbn,
            @RequestParam(defaultValue = "8") int limit) { // 기본값 5
        List<SimilarBookResponse> similarBooks = bookService.getBooksByCategory(isbn, limit);
        return ResponseEntity.ok(ApiResponse.success("OK", similarBooks));
    }

    @GetMapping("/{childId}")
    public ResponseEntity<?> getRecentlyBooks(
            @AuthenticationPrincipal(expression = "member") Member member,
            @PathVariable Long childId,
            @RequestParam(defaultValue = "0") int page
    ) {
        BookListResponse response = bookService.getRecentlyViewedBooks(page, childId, member);

        return ResponseEntity.ok().body(ApiResponse.success("OK", response));
    }

    @GetMapping("/detail/{isbn}")
    public ResponseEntity<ApiResponse<BookDetailResponse>> getDetailBook(@PathVariable String isbn) {
        BookDetailResponse bookDetail = bookService.getBookDetail(isbn);
        return ResponseEntity.ok().body(ApiResponse.success("OK", bookDetail));
    }

    @GetMapping("/read/{isbn}")
    public ResponseEntity<?> getReadBook(
            @AuthenticationPrincipal(expression = "member") Member member,
            @PathVariable String isbn,
            @RequestParam Long childId
    ) {
        ReadBookResponse readBook = bookService.readBook(isbn, childId, member);
        return ResponseEntity.ok().body(ApiResponse.success("OK", readBook));
    }

}
