package com.eureka.mindbloom.common.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.eureka.mindbloom.recommend.repository.RecommendCacheService;
import com.eureka.mindbloom.recommend.service.RecommendServiceImpl;

import lombok.RequiredArgsConstructor;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class RedisRefreshScheduler {
	private final RecommendCacheService recommendCacheService;
	private final RecommendServiceImpl recommendServiceImpl;

	@Scheduled(cron = "0 0 1 * * ?")
	public void refreshRedis() {
		deleteCache();
		fetchCache();
	}

	private void fetchCache() {
		recommendServiceImpl.getAllPreferencesBooks();
		recommendServiceImpl.getAllTraitBooks();
		recommendServiceImpl.getTopViewedBooks();
		recommendServiceImpl.getAllSimilarTraitLikeBooks();
	}

	private void deleteCache() {
		recommendCacheService.deleteCache("CategoryBooks:*");
		recommendCacheService.deleteCache("TraitBooks:*");
		recommendCacheService.deleteCache("TopBook:*");
		recommendCacheService.deleteCache("TraitBooksLike:*");
	}
}
