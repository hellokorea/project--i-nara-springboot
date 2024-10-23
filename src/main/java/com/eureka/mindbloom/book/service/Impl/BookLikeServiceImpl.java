package com.eureka.mindbloom.book.service.Impl;

import com.eureka.mindbloom.book.domain.Book;
import com.eureka.mindbloom.book.domain.BookLike;
import com.eureka.mindbloom.book.domain.BookChildId;
import com.eureka.mindbloom.book.repository.BookLikeRepository;
import com.eureka.mindbloom.book.repository.BookRepository;
import com.eureka.mindbloom.book.service.BookLikeService;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.repository.ChildRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookLikeServiceImpl implements BookLikeService {

    private final BookLikeRepository bookLikeRepository;
    private final ChildRepository childRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public BookLike addLike(String isbn, Long childId, String type) {
        Book book = bookRepository.findBookByIsbn(isbn) // 메서드 이름 수정
                .orElseThrow(() -> new EntityNotFoundException("해당 도서를 찾을 수 없습니다."));
        Child child = childRepository.findChildById(childId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        BookLike bookLike = BookLike.builder()
                .type(type)
                .book(book)
                .child(child)
                .build();
        return bookLikeRepository.save(bookLike);
    }
}
