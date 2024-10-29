package com.eureka.mindbloom.recommend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eureka.mindbloom.book.domain.BookCategory;
import com.eureka.mindbloom.book.dto.BookRecommendResponse;
import com.eureka.mindbloom.book.repository.BookCategoryRepository;
import com.eureka.mindbloom.category.repository.ChildPreferredRepository;
import com.eureka.mindbloom.commoncode.domain.CommonCode;
import com.eureka.mindbloom.commoncode.domain.CommonCodeGroup;
import com.eureka.mindbloom.commoncode.service.CommonCodeConvertService;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.recommend.domain.BookRecommend;
import com.eureka.mindbloom.recommend.domain.BookRecommendLike;
import com.eureka.mindbloom.recommend.eums.RecommendLikeType;
import com.eureka.mindbloom.recommend.repository.BookRecommendLikeRepository;
import com.eureka.mindbloom.recommend.repository.BookRecommendRepository;
import com.eureka.mindbloom.recommend.repository.RecommendCacheService;
import com.eureka.mindbloom.trait.service.ChildRecordHistoryService;
import com.eureka.mindbloom.trait.service.ChildTraitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendServiceImpl implements RecommendService {
	private final BookRecommendRepository bookRecommendRepository;
	private final BookRecommendLikeRepository bookRecommendLikeRepository;
	private final ChildRepository childRepository;
	private final ChildPreferredRepository childPreferredRepository;
	private final CommonCodeConvertService commonCodeConvertService;
	private final RecommendCacheService recommendCacheService;
	private final ChildRecordHistoryService childRecordHistoryService;
	private final BookCategoryRepository bookCategoryRepository;
	private final ChildTraitService childTraitService;

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
			saveRecommendBookLike(childId, child, bookRecommend);
			saveTraitRecordHistory(likeType, child, bookRecommend);
			return;
		}
		bookRecommendLikeRepository.deleteById(bookRecommendId);
	}

	private void saveTraitRecordHistory(RecommendLikeType likeType, Child child, BookRecommend bookRecommend) {
		String code = commonCodeConvertService.codeNameToCommonCode(likeType.getActionName()).getCode();
		String isbn = bookRecommend.getBook().getIsbn();
		BookCategory bookCategory = bookCategoryRepository.findByIsbn(isbn);
		int point = bookCategory.getCategoryTrait().getWeight();
		childRecordHistoryService.createChildTraitHistory(child, code, bookCategory.getTraitCode(), point);
	}

	private void saveRecommendBookLike(Long childId, Child child, BookRecommend bookRecommend) {
		String childTrait = childTraitService.getChildTraitValue(childId).getTraitValue();
		bookRecommendLikeRepository.save(new BookRecommendLike(bookRecommend, child,childTrait));
	}

	@Override
	public List<String> getPreferencesBooksByChildId(Long childId) {
		List<String> books = new LinkedList<>();
		List<String> preferences = childPreferredRepository.findCategoryCodeByChildId(childId);

		preferences.forEach(preference -> {
			commonCodeConvertService.codeGroupToCommonCodes(preference).stream().map(CommonCode::getCode)
				.forEach(categoryCode -> {
					books.addAll(recommendCacheService.getCategoryBooks(categoryCode));
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
			books.addAll(recommendCacheService.getTraitBooks(traitCode));
		});

		return books;
	}

	@Override
	public List<String> getTopViewedBooks() {
		return recommendCacheService.getTop10ViewedBook();
	}

	@Override
	public List<String> getSimilarTraitLikeBooks(Long childId) {
		// TODO : 자녀 성향 공통코드 조회 -> 아직 자녀 성향 누적 테이블이 존재하지 않아 임시 성향을 고정으로 조회
		String traitValue = "ESTP";
		return recommendCacheService.getTraitBooksLike(traitValue);
	}

	public List<String> getAllPreferencesBooks() {

		List<String> books = new LinkedList<>();
		List<String> preferences = commonCodeConvertService.parentCodeGroupToCodeGroups("0200").stream().map(CommonCodeGroup::getCodeGroup).toList();

		preferences.forEach(preference -> {
			commonCodeConvertService.codeGroupToCommonCodes(preference).stream().map(CommonCode::getCode)
				.forEach(categoryCode -> {
					books.addAll(recommendCacheService.getCategoryBooks(categoryCode));
				});
		});

		return books;
	}

	public List<String> getAllSimilarTraitLikeBooks() {

		List<String> books = new LinkedList<>();
		String[] traits = new String[] {"INTP", "INTJ", "ENTP", "ENTJ", "INFJ", "INFP", "ENFP", "ENFJ", "ISTJ", "ISFJ", "ESTJ", "ESFJ", "ISTP", "ISFP", "ESTP", "ESFP"};

		Arrays.stream(traits).forEach(trait -> {
			books.addAll(recommendCacheService.getTraitBooksLike(trait));
				});

		return books;
	}

	public List<String> getAllTraitBooks() {

		List<String> books = new LinkedList<>();
		List<String> traits = commonCodeConvertService.parentCodeGroupToCodeGroups("0100").stream().map(CommonCodeGroup::getCodeGroup).toList();

		traits.forEach(trait -> {
			commonCodeConvertService.codeGroupToCommonCodes(trait).stream().map(CommonCode::getCode)
				.forEach(traitCode -> {
					books.addAll(recommendCacheService.getTraitBooks(traitCode));
				});
		});

		return books;
	}

}
