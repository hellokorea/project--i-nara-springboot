package com.eureka.mindbloom.book.controller;

import com.eureka.mindbloom.book.dto.BooksResponse;
import com.eureka.mindbloom.book.service.BookService;
import com.eureka.mindbloom.book.service.SortOption;
import com.eureka.mindbloom.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member/books")
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
}
