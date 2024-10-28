package com.eureka.mindbloom.book.service.Impl;

import com.eureka.mindbloom.book.domain.*;
import com.eureka.mindbloom.book.dto.BookLikeStatsResponse;
import com.eureka.mindbloom.book.exception.DuplicateLikeException;
import com.eureka.mindbloom.book.repository.BookLikeRepository;
import com.eureka.mindbloom.book.repository.BookLikeStatsRepository;
import com.eureka.mindbloom.book.repository.BookRepository;
import com.eureka.mindbloom.book.service.BookLikeService;
import com.eureka.mindbloom.book.type.LikeOperation;
import com.eureka.mindbloom.common.exception.NotFoundException;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookLikeServiceImpl implements BookLikeService {

    private final BookLikeRepository bookLikeRepository;
    private final BookLikeStatsRepository bookLikeStatsRepository;
    private final ChildRepository childRepository;
    private final BookRepository bookRepository;

    private static final String LIKE_CODE = "0300_02";  // 좋아요 코드

    @Override
    @Transactional
    public BookLike addLike(String isbn, Long childId, String type) {
        Book book = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> NotFoundException.bookNotFound(isbn));
        Child child = childRepository.findChildById(childId)
                .orElseThrow(() -> NotFoundException.childNotFound(childId));

        BookChildId bookChildId = new BookChildId(isbn, childId);
        if (bookLikeRepository.findById(bookChildId).isPresent()) {
            throw DuplicateLikeException.likeAlreadyExists(isbn);
        }

        BookLike bookLike = BookLike.builder()
                .type(type)
                .book(book)
                .child(child)
                .build();

        updateLikeStats(book, LikeOperation.ADD);
        return bookLikeRepository.save(bookLike);
    }

    @Override
    @Transactional
    public void removeLike(String isbn, Long childId) {
        BookChildId bookChildId = new BookChildId(isbn, childId);

        BookLike bookLike = bookLikeRepository.findById(bookChildId)
                .orElseThrow(() -> NotFoundException.bookLikeNotFound(isbn));

        updateLikeStats(bookLike.getBook(), LikeOperation.REMOVE);
        bookLikeRepository.delete(bookLike);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookLikeStatsResponse> getBookLikeStats(String isbn) {
        return bookLikeStatsRepository.likeCountByIsbn(isbn).stream()
                .filter(stats -> LIKE_CODE.equals(stats.getId().getType()))
                .map(stats -> BookLikeStatsResponse.builder()
                        .isbn(stats.getBook().getIsbn())
                        .type(stats.getId().getType())
                        .count(stats.getCount())
                        .build())
                .collect(Collectors.toList());
    }

    private void updateLikeStats(Book book, LikeOperation operation){
        BookLikeStatsId statsId = new BookLikeStatsId(book.getIsbn(), LIKE_CODE);
        BookLikeStats bookLikeStats = bookLikeStatsRepository.findById(statsId)
                .orElse(BookLikeStats.builder()
                        .book(book)
                        .type(LIKE_CODE)
                        .count(0L)
                        .build());

        long newCount = operation.calculateCount(bookLikeStats.getCount());

        BookLikeStats updatedStats = BookLikeStats.builder()
                .book(book)
                .type(LIKE_CODE)
                .count(newCount)
                .build();

        bookLikeStatsRepository.save(updatedStats);
    }
}
