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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookLikeServiceImpl implements BookLikeService {
    private final BookLikeRepository bookLikeRepository;
    private final BookLikeStatsRepository bookLikeStatsRepository;
    private final ChildRepository childRepository;
    private final BookRepository bookRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String LIKE_CODE = "0300_02";
    private static final String STATS_CACHE_KEY_PREFIX = "bookLikes::stats:";
    private static final String LIKE_CACHE_KEY_PREFIX = "bookLikes::like:";
    private static final String LIKE_EXISTS = "1";
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    @Override
    @Transactional
    public BookLike addLike(String isbn, Long childId, String type) {
        String likeKey = generateLikeKey(isbn, childId);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // Redis에서 먼저 중복 체크
        if (LIKE_EXISTS.equals(ops.get(likeKey))) {
            throw DuplicateLikeException.likeAlreadyExists(isbn);
        }

        // Redis에 좋아요 정보 저장
        ops.set(likeKey, LIKE_EXISTS, CACHE_TTL);
        ops.increment(STATS_CACHE_KEY_PREFIX + isbn);

        // 백그라운드에서 DB 저장
        Book book = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> NotFoundException.bookNotFound(isbn));
        Child child = childRepository.findChildById(childId)
                .orElseThrow(() -> NotFoundException.childNotFound(childId));

        BookLike savedLike = bookLikeRepository.save(
                BookLike.builder()
                        .type(type)
                        .book(book)
                        .child(child)
                        .build()
        );

        updateLikeStats(book, LikeOperation.ADD);

        return savedLike;
    }

    @Override
    @Transactional
    public void removeLike(String isbn, Long childId) {
        String likeKey = generateLikeKey(isbn, childId);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // Redis에서 좋아요 정보 삭제
        if (Boolean.TRUE.equals(redisTemplate.delete(likeKey))) {
            ops.decrement(STATS_CACHE_KEY_PREFIX + isbn);
        }

        // 백그라운드에서 DB 삭제
        BookChildId bookChildId = new BookChildId(isbn, childId);
        bookLikeRepository.findById(bookChildId).ifPresent(like -> {
            bookLikeRepository.delete(like);
            updateLikeStats(like.getBook(), LikeOperation.REMOVE);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookLikeStatsResponse> getBookLikeStats(String isbn) {
        String statsKey = STATS_CACHE_KEY_PREFIX + isbn;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // Redis에서 조회
        String count = ops.get(statsKey);
        if (count != null) {
            return Collections.singletonList(
                    BookLikeStatsResponse.builder()
                            .isbn(isbn)
                            .type(LIKE_CODE)
                            .count(Long.parseLong(count))
                            .build()
            );
        }

        // Redis에 없는 경우에만 DB 조회 및 캐시 업데이트
        List<BookLikeStatsResponse> stats = bookLikeStatsRepository.likeCountByIsbn(isbn).stream()
                .map(stat -> BookLikeStatsResponse.builder()
                        .isbn(stat.getBook().getIsbn())
                        .type(stat.getId().getType())
                        .count(stat.getCount())
                        .build())
                .collect(java.util.stream.Collectors.toList());

        if (!stats.isEmpty()) {
            BookLikeStatsResponse stat = stats.get(0);
            ops.set(statsKey, String.valueOf(stat.getCount()), CACHE_TTL);
        }

        return stats;
    }

    protected void updateLikeStats(Book book, LikeOperation operation) {
        BookLikeStatsId statsId = new BookLikeStatsId(book.getIsbn(), LIKE_CODE);
        BookLikeStats stats = bookLikeStatsRepository.findById(statsId)
                .orElse(BookLikeStats.builder()
                        .book(book)
                        .type(LIKE_CODE)
                        .count(0L)
                        .build());

        stats.updateCount(operation.calculateCount(stats.getCount()));

        if (stats.getId() == null) {
            bookLikeStatsRepository.save(stats);
        }
    }

    protected String generateLikeKey(String isbn, Long childId) {
        return LIKE_CACHE_KEY_PREFIX + isbn + ":" + childId;
    }
}