package com.eureka.mindbloom.recommend.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eureka.mindbloom.book.dto.BookRecommendResponse;
import com.eureka.mindbloom.category.repository.ChildPreferredRepository;
import com.eureka.mindbloom.commoncode.domain.CommonCode;
import com.eureka.mindbloom.commoncode.service.CommonCodeConvertService;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.recommend.domain.BookRecommend;
import com.eureka.mindbloom.recommend.domain.BookRecommendLike;
import com.eureka.mindbloom.recommend.eums.RecommendLikeType;
import com.eureka.mindbloom.recommend.repository.BookRecommendLikeRepository;
import com.eureka.mindbloom.recommend.repository.BookRecommendRepository;
import com.eureka.mindbloom.recommend.repository.RecommendCacheRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
	private final BookRecommendRepository bookRecommendRepository;
	private final BookRecommendLikeRepository bookRecommendLikeRepository;
	private final ChildRepository childRepository;
	private final ChildPreferredRepository childPreferredRepository;
	private final CommonCodeConvertService commonCodeConvertService;
	private final RecommendCacheRepository recommendCacheRepository;

	@Override
	public List<BookRecommendResponse> getRecommendBooks(Long childId) {
		List<BookRecommendResponse> books = bookRecommendRepository.findByChildIdAndCurrentDate(childId);
		return books;
	}

	@Override
	public void likeRecommendBook(Long childId, Long bookRecommendId, RecommendLikeType likeType) {
		Child child = childRepository.findById(childId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 자녀가 존재하지 않습니다."));
		BookRecommend bookRecommend = bookRecommendRepository.findById(bookRecommendId)
			.orElseThrow(() -> new IllegalArgumentException("해당 아이디의 추천 도서가 존재하지 않습니다."));
		if (likeType == RecommendLikeType.LIKE) {
			bookRecommendLikeRepository.save(new BookRecommendLike(bookRecommend, child));
			return;
		}
		bookRecommendLikeRepository.deleteById(bookRecommendId);
	}

	@Override
	public List<String> getPreferencesBooksByChildId(Long childId) {
		List<String> books = new LinkedList<>();
		List<String> preferences = childPreferredRepository.findCategoryCodeByChildId(childId);

		preferences.forEach(preference -> {
			commonCodeConvertService.codeGroupToCommonCodes(preference).stream().map(CommonCode::getCode)
				.forEach(categoryCode -> {
					books.addAll(recommendCacheRepository.getCategoryBooks(categoryCode));
				});
		});

		return books;
	}

	@Override
	public List<String> getTraitBooksByChildId(Long childId) {
		List<String> books = new LinkedList<>();
		// TODO : 자녀 성향 공통코드 조회 -> 아직 자녀 성향 누적 테이블이 존재하지 않아 임시 성향을 고정으로 조회
		List<String> traitCodes = new ArrayList<>();
		traitCodes.add("0101_01");
		traitCodes.add("0101_03");
		traitCodes.add("0101_05");
		traitCodes.add("0101_07");

		traitCodes.forEach(traitCode -> {
			books.addAll(recommendCacheRepository.getTraitBooks(traitCode));
		});

		return books;
	}

	@Override
	public List<String> getTopViewedBooks() {
		return recommendCacheRepository.getTop10ViewedBook();
	}

}
