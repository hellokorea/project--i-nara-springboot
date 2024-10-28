package com.eureka.mindbloom.common.exception;

import com.eureka.mindbloom.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public final ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e, HttpServletRequest request) {
        log.warn("source = {} {}, message = {}",
                request.getMethod(), request.getRequestURI(), e.getMessage(), e);

        return buildResponseEntity(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.info("source = {} {}, message = {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.failure("유효성 검사 실패", errors));
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse<Void>> handleExceptions(Exception e, HttpServletRequest request) {
        log.error("source = {} {}, message = {}",
                request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
    }

    private static ResponseEntity<ApiResponse<Void>> buildResponseEntity(HttpStatusCode statusCode, String message) {
        return ResponseEntity
                .status(statusCode)
                .body(ApiResponse.failure(message));
    }
}