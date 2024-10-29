package com.eureka.mindbloom.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookViewCacheService {

    private static final String VIEW_COUNT_KEY_PREFIX = "book:view:";

    private final StringRedisTemplate redisTemplate;

    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void incrementViewCount(String isbn) {
        String key = VIEW_COUNT_KEY_PREFIX + isbn;
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, 1, TimeUnit.HOURS);  // 만료 시간(1시간)
    }

    public Long getViewCount(String isbn) {
        String key = VIEW_COUNT_KEY_PREFIX + isbn;
        String count = redisTemplate.opsForValue().get(key);
        return count != null ? Long.parseLong(count) : 0L;
    }

    public void removeViewCount(String isbn) {
        String key = VIEW_COUNT_KEY_PREFIX + isbn;
        redisTemplate.delete(key);
    }
}
