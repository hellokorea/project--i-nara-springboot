package com.eureka.mindbloom.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        String message,
        T data,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp) {

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(message, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(message, null, LocalDateTime.now());
    }
}