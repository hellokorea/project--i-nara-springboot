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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookLikeServiceImpl implements BookLikeService {

    private final BookLikeRepository bookLikeRepository;
    private final BookLikeStatsRepository bookLikeStatsRepository;
    private final ChildRepository childRepository;
    private final BookRepository bookRepository;

    private static final String LIKE_CODE = "0300_02";  // 좋아요 코드
    private static final String CACHE_NAME = "bookLikes";


    protected boolean isDuplicateLike(String isbn, Long childId) {
        BookChildId bookChildId = new BookChildId(isbn, childId);
        return bookLikeRepository.findById(bookChildId).isPresent();
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "'stats:' + #isbn")
    public BookLike addLike(String isbn, Long childId, String type) {
        log.info("Adding Like for book ISBN: {}, child: {}", isbn, childId);

        Book book = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> NotFoundException.bookNotFound(isbn));
        Child child = childRepository.findChildById(childId)
                .orElseThrow(() -> NotFoundException.childNotFound(childId));

        // 데이터베이스 저장 전 중복 좋아요 검사
        if (isDuplicateLike(isbn, childId)) {
            throw DuplicateLikeException.likeAlreadyExists(isbn);
        }

        BookLike savedLike = bookLikeRepository.save(
                BookLike.builder()
                        .type(type)
                        .book(book)
                        .child(child)
                        .build()
        );

        // 트랜잭션이 완료된 후 Redis 캐시 업데이트
        updateLikeStatsAfterTransaction(book, LikeOperation.ADD);

        log.info("Like added successfully for book ISBN: {}", isbn);
        return savedLike;
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "'stats:' + #isbn")
    public void removeLike(String isbn, Long childId) {
        log.info("Removing like for book ISBN: {}, childId: {}", isbn, childId);

        BookChildId bookChildId = new BookChildId(isbn, childId);

        BookLike bookLike = bookLikeRepository.findById(bookChildId)
                .orElseThrow(() -> NotFoundException.bookLikeNotFound(isbn));

        updateLikeStats(bookLike.getBook(), LikeOperation.REMOVE);
        bookLikeRepository.delete(bookLike);
        log.info("Like removed successfully for book ISBN: {}", isbn);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_NAME, key = "'stats:' + #isbn")
    public List<BookLikeStatsResponse> getBookLikeStats(String isbn) {
        log.info("Fetching like stats for book ISBN: {}", isbn);

        List<BookLikeStatsResponse> stats = bookLikeStatsRepository.likeCountByIsbn(isbn).stream()
                .map(stat -> BookLikeStatsResponse.builder()
                        .isbn(stat.getBook().getIsbn())
                        .type(stat.getId().getType())
                        .count(stat.getCount())
                        .build())
                .collect(Collectors.toList());

        log.info("Found {} like stats for book ISBN: {}", stats.size(), isbn);
        return stats;
    }


    protected void updateLikeStats(Book book, LikeOperation operation){
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
        log.info("Updated like stats for book ISBN: {}, new count: {}", book.getIsbn(), newCount);
    }

    protected void updateLikeStatsAfterTransaction(Book book, LikeOperation operation) {
        updateLikeStats(book, operation);
    }
}
