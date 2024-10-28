package com.eureka.mindbloom.book.controller;

import com.eureka.mindbloom.book.dto.AdminBookRequest;
import com.eureka.mindbloom.book.dto.AdminBookResponse;
import com.eureka.mindbloom.book.service.AdminBookService;
import com.eureka.mindbloom.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final AdminBookService adminBookService;

    // 도서 등록
    @PostMapping("/")
    public ResponseEntity<ApiResponse<AdminBookResponse>> registerBook(@RequestBody AdminBookRequest adminBookRequestDto) {
        AdminBookResponse savedBook = adminBookService.registerBook(adminBookRequestDto);
        return ResponseEntity.ok(ApiResponse.success("도서가 성공적으로 등록되었습니다.", savedBook));
    }

    // 도서 수정
    @PutMapping("/{isbn}")
    public ResponseEntity<ApiResponse<AdminBookResponse>> updateBook(@PathVariable String isbn, @RequestBody AdminBookRequest adminBookRequestDto) {
        Optional<AdminBookResponse> updatedBook = adminBookService.updateBook(isbn, adminBookRequestDto);
        return updatedBook.map(book -> ResponseEntity.ok(ApiResponse.success("도서가 성공적으로 수정되었습니다.", book)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.failure("도서를 찾을 수 없습니다.")));
    }

    // 도서 삭제
    @DeleteMapping("/{isbn}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable String isbn) {
        adminBookService.deleteBook(isbn);
        return ResponseEntity.ok(ApiResponse.success("도서가 성공적으로 삭제되었습니다."));
    }
}
