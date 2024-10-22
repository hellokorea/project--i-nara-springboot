package com.eureka.mindbloom.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ChildRegisterRequest(
        @NotNull String name,
        @NotNull LocalDate birthDate,
        @NotNull String gender,
        @Size(max = 3, message = "카테고리는 최대 3개까지 선택 가능합니다.") List<String> categories
) {
}