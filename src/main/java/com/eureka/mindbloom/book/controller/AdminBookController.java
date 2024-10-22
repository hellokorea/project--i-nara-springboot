package com.eureka.mindbloom.book.controller;

import com.eureka.mindbloom.book.dto.AdminBookRequestDto;
import com.eureka.mindbloom.book.dto.AdminBookResponseDto;
import com.eureka.mindbloom.book.service.AdminBookService;
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
    public ResponseEntity<AdminBookResponseDto> registerBook(@RequestBody AdminBookRequestDto adminBookRequestDto) {
        AdminBookResponseDto savedBook = adminBookService.registerBook(adminBookRequestDto);
        return ResponseEntity.ok(savedBook);
    }

    // 도서 수정
    @PutMapping("/{isbn}")
    public ResponseEntity<AdminBookResponseDto> updateBook(@PathVariable String isbn, @RequestBody AdminBookRequestDto adminBookRequestDto) {
        Optional<AdminBookResponseDto> updatedBook = adminBookService.updateBook(isbn, adminBookRequestDto);
        return updatedBook.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 도서 삭제
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        adminBookService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }
}
