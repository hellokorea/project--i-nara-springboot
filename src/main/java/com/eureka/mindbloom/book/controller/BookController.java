package com.eureka.mindbloom.book.controller;

import com.eureka.mindbloom.book.dto.BookDetailResponse;
import com.eureka.mindbloom.book.dto.BooksResponse;
import com.eureka.mindbloom.book.dto.RecentlyBookResponse;
import com.eureka.mindbloom.book.service.BookService;
import com.eureka.mindbloom.book.type.SortOption;
import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ApiResponse<Slice<BooksResponse>> getBooks(
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) SortOption sortOption
    ) {
        Slice<BooksResponse> books = bookService.getBooks(categoryCode, search, page, sortOption);
        return ApiResponse.success("OK", books);
    }

    @GetMapping("/{childId}")
    public ResponseEntity<?> getRecentlyBooks(
            @AuthenticationPrincipal(expression = "member") Member member,
            @PathVariable Long childId,
            @RequestParam(defaultValue = "0") int page
    ) {
        RecentlyBookResponse response = bookService.getRecentlyViewedBooks(page, childId, member);

        return ResponseEntity.ok().body(ApiResponse.success("OK", response));
    }

    @GetMapping("/detail/{isbn}")
    public ResponseEntity<?> getDetailBook(@PathVariable String isbn) {
        BookDetailResponse bookDetail = bookService.getBookDetail(isbn);
        return ResponseEntity.ok().body(ApiResponse.success("OK", bookDetail));
    }
}
