package com.eureka.mindbloom.book.dto;

import java.util.List;

public record BookListResponse(
        List<BooksResponse> books,
        boolean isLast
) {
}