package com.eureka.mindbloom.book.controller;

import com.eureka.mindbloom.book.dto.AdminBookRequest;
import com.eureka.mindbloom.book.dto.AdminBookResponse;
import com.eureka.mindbloom.book.service.AdminBookService;
import com.eureka.mindbloom.book.util.BookFileProcessor;
import com.eureka.mindbloom.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final AdminBookService adminBookService;
    private final BookFileProcessor bookFileProcessor;  // BookFileProcessor 주입

    // 도서 등록
    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ApiResponse<AdminBookResponse>> registerBook(@RequestBody AdminBookRequest adminBookRequestDto) {
        AdminBookResponse savedBook = adminBookService.registerBook(adminBookRequestDto);
        return ResponseEntity.ok(ApiResponse.success("도서가 성공적으로 등록되었습니다.", savedBook));
    }

    // 서버 경로에 있는 CSV 파일을 사용하여 도서 벌크 등록
    @PostMapping("/csv")//post로
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ApiResponse<String>> processCsvFile(@RequestParam String filePath) {
        try {
            bookFileProcessor.processCsvFileFromPath(filePath);
            return ResponseEntity.ok(ApiResponse.success("CSV 파일이 성공적으로 처리되었습니다.", "파일 경로: " + filePath));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(ApiResponse.failure("CSV 파일 처리 중 오류 발생: " + e.getMessage()));
        }
    }

    // 도서 수정
    @PutMapping("/{isbn}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<AdminBookResponse>> updateBook(@PathVariable String isbn, @RequestBody AdminBookRequest adminBookRequestDto) {
        Optional<AdminBookResponse> updatedBook = adminBookService.updateBook(isbn, adminBookRequestDto);
        return updatedBook.map(book -> ResponseEntity.ok(ApiResponse.success("도서가 성공적으로 수정되었습니다.", book)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.failure("도서를 찾을 수 없습니다.")));
    }

    // 도서 삭제
    @DeleteMapping("/{isbn}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable String isbn) {
        adminBookService.deleteBook(isbn);
        return ResponseEntity.ok(ApiResponse.success("도서가 성공적으로 삭제되었습니다."));
    }
}
