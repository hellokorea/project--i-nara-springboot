package com.eureka.mindbloom.book.dto;

import java.util.List;

public record RecentlyBookResponse(
        List<BooksResponse> books,
        boolean isLast
) {
}