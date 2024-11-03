package com.eureka.mindbloom.common.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.eureka.mindbloom.recommend.repository.RecommendCacheService;
import com.eureka.mindbloom.recommend.service.RecommendService;
import com.eureka.mindbloom.recommend.service.RecommendServiceImpl;

import lombok.RequiredArgsConstructor;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class RedisRefreshScheduler {
	private final RecommendCacheService recommendCacheService;
	private final RecommendService recommendService;

	@Scheduled(cron = "0 0 1 * * ?")
	public void refreshRedis() {
		deleteCache();
		fetchCache();
	}

	private void fetchCache() {
		recommendService.getAllPreferencesBooks();
		recommendService.getAllTraitBooks();
		recommendService.getTopViewedBooks();
		recommendService.getAllSimilarTraitLikeBooks();
	}

	private void deleteCache() {
		recommendCacheService.deleteCache("CategoryBooks:*");
		recommendCacheService.deleteCache("TraitBooks:*");
		recommendCacheService.deleteCache("TopBook:*");
		recommendCacheService.deleteCache("TraitBooksLike:*");
	}
}
