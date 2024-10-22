package com.eureka.mindbloom.book.service;

import com.eureka.mindbloom.book.domain.Book;
import com.eureka.mindbloom.book.dto.AdminBookRequestDto;
import com.eureka.mindbloom.book.dto.AdminBookResponseDto;
import com.eureka.mindbloom.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminBookServiceImpl implements AdminBookService {

    private final BookRepository bookRepository;

    @Override
    public AdminBookResponseDto registerBook(AdminBookRequestDto adminBookRequestDto) {
        Book book = adminBookRequestDto.toEntity();
        Book savedBook = bookRepository.save(book);
        return new AdminBookResponseDto(savedBook);
    }

    @Override
    public Optional<AdminBookResponseDto> updateBook(String isbn, AdminBookRequestDto adminBookRequestDto) {
        // ISBN으로 찾기
        return bookRepository.findById(isbn).map(existingBook -> {
            existingBook.updateDetails(adminBookRequestDto.toEntity()); // 기존 책 정보를 업데이트
            Book updatedBook = bookRepository.save(existingBook);  // 저장
            return new AdminBookResponseDto(updatedBook);               // DTO로 반환
        });
    }

    @Override
    public void deleteBook(String isbn) {
        // ISBN으로 삭제
        bookRepository.deleteById(isbn);
    }

}
