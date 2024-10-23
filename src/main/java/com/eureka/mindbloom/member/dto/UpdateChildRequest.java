package com.eureka.mindbloom.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateChildRequest(
        @NotNull String name,
        @Size(max = 3, message = "카테고리는 최대 3개까지 선택 가능합니다.") List<String> categories
) {
}