package com.eureka.mindbloom.book.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminBookCategoryDto {

    private String category;  // 카테고리 이름을 나타냄

    @Builder
    public AdminBookCategoryDto(String category) {
        this.category = category;
    }

    // 카테고리 이름을 반환하는 getter
    public String getCategoryName() {
        return this.category;  // 카테고리 이름 반환
    }
}
