package com.eureka.mindbloom.recommend.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.eureka.mindbloom.book.repository.BookCategoryRepository;
import com.eureka.mindbloom.book.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecommendCacheRepository {
	private final BookCategoryRepository bookCategoryRepository;
	private final BookRepository bookRepository;
	private final BookRecommendLikeRepository bookRecommendLikeRepository;

	@Cacheable(cacheNames = "CategoryBooks", key = "#categoryCode", cacheManager = "redisCacheManager")
	public List<String> getCategoryBooks(String categoryCode) {
		return bookCategoryRepository.findIsbnByCategoryCodes(categoryCode);
	}

	@Cacheable(cacheNames = "TraitBooks", key = "#traitCode", cacheManager = "redisCacheManager")
	public List<String> getTraitBooks(String traitCode) {
		return bookCategoryRepository.findIsbnByTraitCode(traitCode);
	}

	@Cacheable(cacheNames = "TopBook", key = "'Top10ViewedBook'", cacheManager = "redisCacheManager")
	public List<String> getTop10ViewedBook() {
		return bookRepository.findTop10IsbnByOrderByViewCount();
	}

	@Cacheable(cacheNames = "TraitBooksLike", key = "#trait", cacheManager = "redisCacheManager")
	public List<String> getTraitBooksLike(String trait) {
		return bookRecommendLikeRepository.findIsbnByTraitValue(trait);
	}
}
