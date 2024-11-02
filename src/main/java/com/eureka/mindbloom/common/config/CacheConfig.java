package com.eureka.mindbloom.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(name = "commonCodeCacheManager")
    public CaffeineCacheManager commonCodeCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("codeGroup", "commonCode");
        cacheManager.setCaffeine(Caffeine.newBuilder().recordStats());
        return cacheManager;
    }

    @Bean(name = "loginCacheManager")
    public CaffeineCacheManager loginCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("loginMember");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .recordStats());
        return cacheManager;
    }
}