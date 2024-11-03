package com.eureka.mindbloom.recommend.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.eureka.mindbloom.book.domain.BookCategory;
import com.eureka.mindbloom.book.dto.BookRecommendResponse;
import com.eureka.mindbloom.book.repository.BookCategoryRepository;
import com.eureka.mindbloom.book.repository.BookRepository;
import com.eureka.mindbloom.book.repository.BookViewRepository;
import com.eureka.mindbloom.book.repository.jdbc.BookRecommendBulkRepository;
import com.eureka.mindbloom.category.repository.ChildPreferredRepository;
import com.eureka.mindbloom.common.dto.Pair;
import com.eureka.mindbloom.commoncode.domain.CommonCode;
import com.eureka.mindbloom.commoncode.domain.CommonCodeGroup;
import com.eureka.mindbloom.commoncode.service.CommonCodeConvertService;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.exception.ChildNotFoundException;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.recommend.domain.BookRecommend;
import com.eureka.mindbloom.recommend.domain.BookRecommendLike;
import com.eureka.mindbloom.recommend.dto.ChildBooks;
import com.eureka.mindbloom.recommend.eums.RecommendLikeType;
import com.eureka.mindbloom.recommend.repository.BookRecommendLikeRepository;
import com.eureka.mindbloom.recommend.repository.BookRecommendRepository;
import com.eureka.mindbloom.recommend.repository.RecommendCacheService;
import com.eureka.mindbloom.trait.enums.TraitValue;
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
	private final BookRepository bookRepository;

	private final BookRecommendBulkRepository bookRecommendBulkRepository;
	private final BookViewRepository bookViewRepository;


	private final int needBookCount = 10;
	private final int preferredWeight = 3;
	private final int traitWeight = 2;
	private final int similarTraitLikeWeight = 1;


	@Override
	public List<BookRecommendResponse> getRecommendBooks(Long childId) {
		List<BookRecommendResponse> books = bookRecommendRepository.findByChildIdAndCurrentDate(childId);
		if(books.isEmpty()){
			books = bookRepository.findByIsbns(getTopViewedBooks());
		}
		return books;
	}

	@Override
	public void likeRecommendBook(Long childId, Long bookRecommendId, RecommendLikeType likeType) {
		Child child = childRepository.findById(childId).orElseThrow(() -> new ChildNotFoundException(childId));
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
		childRecordHistoryService.createChildTraitHistory(child, code, bookCategory.getTraitCode(), point, bookRecommend.getBook().getTitle());
	}

	private void saveRecommendBookLike(Long childId, Child child, BookRecommend bookRecommend) {
		String traitValue = childTraitService.getChildTraitValue(childId).getTraitValue();
		bookRecommendLikeRepository.save(new BookRecommendLike(bookRecommend, child,traitValue));
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
		Map<String,Integer> traitScoreRecords = childTraitService.getTraitScoreRecords(childRepository.findById(childId).orElseThrow(() -> new ChildNotFoundException(childId)));
		List<String> traitCodes = new ArrayList<>();
		traitScoreRecords.forEach((traitCode, score) -> {
			if (score > 50) {
				traitCodes.add(commonCodeConvertService.codeNameToCommonCode(traitCode).getCode());
			}
		});

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
		String traitValue = childTraitService.getChildTraitValue(childId).getTraitValue();
		return recommendCacheService.getTraitBooksLike(traitValue);
	}

	@Override
	public void createRecommendBooks(Long childId) {
		ChildBooks childBooks = findPreferredRecentBooksProcessor(childId);
		childBooks = findPreferredBooksProcessor(childBooks);
		childBooks = findTraitBooksProcessor(childBooks);
		childBooks = findSimilarTraitLikeBooksProcessor(childBooks);
		childBooks = excludeReadBooksProcessor(childBooks);
		childBooks = excludeNotReadRecommendBooksProcessor(childBooks);
		childBooks = extractRecommendBooksProcessor(childBooks);
		bookRecommendBulkRepository.saveAll(childBooks);
	}


	private ChildBooks findPreferredRecentBooksProcessor(Long childId) {
		List<String> preferencesGroupCode = childPreferredRepository.findCategoryCodeByChildId(childId);
		List<String> preferences = new ArrayList<>();
		preferencesGroupCode.forEach(preference -> {
			preferences.addAll(commonCodeConvertService.codeGroupToCommonCodes(preference).stream().map(CommonCode::getCode).toList());
		});

		List<String> booksIsbn;
		if (preferences.size() > 0) {
			booksIsbn = bookRepository.findIsbnByCategoryCodeSortRecent(preferences);
			return new ChildBooks(childId, booksIsbn);
		}
		booksIsbn = new ArrayList<>();
		return new ChildBooks(childId, booksIsbn);
	}

	public ChildBooks findPreferredBooksProcessor(ChildBooks childBooks) {
		List<String> preferredBooksIsbn = getPreferencesBooksByChildId(childBooks.childId);
		if (!preferredBooksIsbn.isEmpty()) {
			Map<String, Integer> traitBooks = childBooks.getTraitBooksIsbn();
			preferredBooksIsbn.stream().forEach(isbn -> traitBooks.put(isbn, preferredWeight));
		}
		return childBooks;
	}

	public ChildBooks findTraitBooksProcessor(ChildBooks childBooks) {
		List<String> traitBooks = getTraitBooksByChildId(childBooks.childId);
		if (!traitBooks.isEmpty()) {
			Map<String, Integer> traitBooksIsbn = childBooks.getTraitBooksIsbn();
			traitBooks.stream().forEach(isbn -> traitBooksIsbn.put(isbn, traitBooksIsbn.getOrDefault(isbn, 0) + traitWeight));
		}
		return childBooks;
	}

	public ChildBooks findSimilarTraitLikeBooksProcessor(ChildBooks childBooks) {
		List<String> similarTraitBooks = getSimilarTraitLikeBooks(childBooks.childId);
		if (!similarTraitBooks.isEmpty()) {
			Map<String, Integer> traitBooksIsbn = childBooks.getTraitBooksIsbn();
			similarTraitBooks.stream().forEach(isbn -> traitBooksIsbn.put(isbn, traitBooksIsbn.getOrDefault(isbn, 0) + similarTraitLikeWeight));
		}
		return childBooks;
	}

	public ChildBooks excludeReadBooksProcessor(ChildBooks childBooks) {
		if (childBooks.getTraitBooksIsbn().size() >= needBookCount) {
			List<String> readBooks = bookViewRepository.findReadIsbnByChildId(childBooks.getChildId());
			Map<String, Integer> traitBooksIsbn = childBooks.getTraitBooksIsbn();
			traitBooksIsbn.keySet().removeAll(readBooks);
		}
		return childBooks;
	}

	public ChildBooks excludeNotReadRecommendBooksProcessor(ChildBooks childBooks) {
		if (childBooks.getTraitBooksIsbn().size() >= needBookCount) {
			List<String> notReadRecommendBooks = bookRecommendRepository.findByNotReadRecommendBooks(childBooks.getChildId());
			Map<String, Integer> traitBooksIsbn = childBooks.getTraitBooksIsbn();
			if (traitBooksIsbn.size() - notReadRecommendBooks.size() >= needBookCount) {
				traitBooksIsbn.keySet().removeAll(notReadRecommendBooks);
			}

		}
		return childBooks;
	}


	public ChildBooks extractRecommendBooksProcessor(ChildBooks childBooks) {
		if (childBooks.getTraitBooksIsbn().size() >= needBookCount) {
			List<String> recommendBooks = childBooks.getRecommendBooksIsbn();
			PriorityQueue<Pair<String, Integer>> priorityQueue = new PriorityQueue<>((first, second) -> second.getB() - first.getB());
			priorityQueue.addAll(childBooks.getTraitBooksIsbn().entrySet().stream().map(entry -> new Pair<>(entry.getKey(), entry.getValue())).toList());
			while (recommendBooks.size() < needBookCount) {
				recommendBooks.add(priorityQueue.poll().getA());
			}
			return childBooks;
		}
		childBooks.setRecommendBooksIsbn(getTopViewedBooks());
		return childBooks;
	}



	@Override
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
		List<String> traits = TraitValue.getTraitList();

		traits.forEach(trait -> {
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
