package com.eureka.mindbloom.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(name = "commonCodeCacheManager")
    public CaffeineCacheManager commonCodeCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("codeGroup", "commonCode");
        cacheManager.setCaffeine(Caffeine.newBuilder().recordStats());
        return cacheManager;
    }
}