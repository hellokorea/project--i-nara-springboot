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
    private final RedisTemplate<String, String> redisTemplate;

    private static final String LIKE_CODE = "0300_02";
    private static final String STATS_CACHE_KEY_PREFIX = "bookLikes::stats:";
    private static final String LIKE_CACHE_KEY_PREFIX = "bookLikes::like:";
    private static final String LIKE_EXISTS = "1";
    private static final Duration STATS_CACHE_TTL = Duration.ofMinutes(1440); // 24시간

    protected boolean isDuplicateLike(String isbn, Long childId) {
        // Redis에서 먼저 확인
        String likeKey = generateLikeKey(isbn, childId);
        Boolean exists = redisTemplate.hasKey(likeKey);

        if (Boolean.TRUE.equals(exists)) {
            return true;
        }

        // DB에서 확인
        BookChildId bookChildId = new BookChildId(isbn, childId);
        boolean isExists = bookLikeRepository.findById(bookChildId).isPresent();

        // 존재하면 Redis에도 캐시
        if (isExists) {
            redisTemplate.opsForValue().set(likeKey, LIKE_EXISTS);
        }

        return isExists;
    }

    protected String generateLikeKey(String isbn, Long childId) {
        return LIKE_CACHE_KEY_PREFIX + isbn + ":" + childId;
    }

    @Override
    @Transactional
    public BookLike addLike(String isbn, Long childId, String type) {
        log.info("Adding Like for book ISBN: {}, child: {}", isbn, childId);

        // 중복 체크
        if (isDuplicateLike(isbn, childId)) {
            throw DuplicateLikeException.likeAlreadyExists(isbn);
        }

        Book book = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> NotFoundException.bookNotFound(isbn));
        Child child = childRepository.findChildById(childId)
                .orElseThrow(() -> NotFoundException.childNotFound(childId));

        // DB 저장
        BookLike savedLike = bookLikeRepository.save(
                BookLike.builder()
                        .type(type)
                        .book(book)
                        .child(child)
                        .build()
        );

        // Redis 업데이트
        updateLikeCache(isbn, childId, LikeOperation.ADD);

        // DB 통계 업데이트
        updateLikeStats(book, LikeOperation.ADD);

        log.info("Like added successfully for book ISBN: {}", isbn);
        return savedLike;
    }

    @Override
    @Transactional
    public void removeLike(String isbn, Long childId) {
        log.info("Removing like for book ISBN: {}, childId: {}", isbn, childId);

        BookChildId bookChildId = new BookChildId(isbn, childId);
        BookLike bookLike = bookLikeRepository.findById(bookChildId)
                .orElseThrow(() -> NotFoundException.bookLikeNotFound(isbn));

        // Redis 업데이트
        updateLikeCache(isbn, childId, LikeOperation.REMOVE);

        // DB 업데이트
        updateLikeStats(bookLike.getBook(), LikeOperation.REMOVE);
        bookLikeRepository.delete(bookLike);

        log.info("Like removed successfully for book ISBN: {}", isbn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookLikeStatsResponse> getBookLikeStats(String isbn) {
        log.info("Fetching like stats for book ISBN: {}", isbn);

        String statsKey = STATS_CACHE_KEY_PREFIX + isbn;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // Redis에서 먼저 조회
        String cachedCount = ops.get(statsKey);
        if (cachedCount != null) {
            return Collections.singletonList(
                    BookLikeStatsResponse.builder()
                            .isbn(isbn)
                            .type(LIKE_CODE)
                            .count(Long.parseLong(cachedCount))
                            .build()
            );
        }

        // Cache miss - DB에서 조회
        List<BookLikeStatsResponse> stats = bookLikeStatsRepository.likeCountByIsbn(isbn).stream()
                .map(stat -> BookLikeStatsResponse.builder()
                        .isbn(stat.getBook().getIsbn())
                        .type(stat.getId().getType())
                        .count(stat.getCount())
                        .build())
                .collect(Collectors.toList());

        // Redis 캐시 업데이트
        if (!stats.isEmpty()) {
            BookLikeStatsResponse stat = stats.get(0);
            ops.set(statsKey, String.valueOf(stat.getCount()), STATS_CACHE_TTL);
        }

        log.info("Found {} like stats for book ISBN: {}", stats.size(), isbn);
        return stats;
    }

    protected void updateLikeStats(Book book, LikeOperation operation) {
        BookLikeStatsId statsId = new BookLikeStatsId(book.getIsbn(), LIKE_CODE);
        BookLikeStats bookLikeStats = bookLikeStatsRepository.findById(statsId)
                .orElse(BookLikeStats.builder()
                        .book(book)
                        .type(LIKE_CODE)
                        .count(0L)
                        .build());

        // 기존 객체의 count를 직접 수정
        bookLikeStats.updateCount(operation.calculateCount(bookLikeStats.getCount()));

        // 처음 생성된 경우에만 save 호출
        if (bookLikeStats.getId() == null) {
            bookLikeStatsRepository.save(bookLikeStats);
        }

        log.info("Updated like stats for book ISBN: {}, new count: {}",
                book.getIsbn(), bookLikeStats.getCount());
    }

    protected void updateLikeCache(String isbn, Long childId, LikeOperation operation) {
        String statsKey = STATS_CACHE_KEY_PREFIX + isbn;
        String likeKey = generateLikeKey(isbn, childId);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        switch (operation) {
            case ADD:
                ops.set(likeKey, LIKE_EXISTS);
                ops.increment(statsKey);
                redisTemplate.expire(statsKey, STATS_CACHE_TTL);
                break;
            case REMOVE:
                redisTemplate.delete(likeKey); // RedisTemplate.delete() 사용
                ops.decrement(statsKey);
                redisTemplate.expire(statsKey, STATS_CACHE_TTL);
                break;
        }
    }
}