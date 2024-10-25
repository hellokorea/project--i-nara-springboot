package com.eureka.mindbloom.recommend.batch.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.eureka.mindbloom.recommend.dto.ChildBooks;
import com.eureka.mindbloom.book.repository.BookCategoryRepository;
import com.eureka.mindbloom.recommend.repository.BookRecommendRepository;
import com.eureka.mindbloom.book.repository.BookRepository;
import com.eureka.mindbloom.book.repository.jdbc.BookRecommendBulkRepository;
import com.eureka.mindbloom.category.repository.ChildPreferredRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RecommendJobConfiguration {

	private final DataSource dataSource;
	private final BookRepository bookRepository;
	private final ChildPreferredRepository childPreferredRepository;
	private final PlatformTransactionManager transactionManager;
	private final BookCategoryRepository bookCategoryRepository;
	private final BookRecommendRepository bookRecommendRepository;
	private final BookRecommendBulkRepository bookRecommendBulkRepository;

	private final int needBookCount = 10;

	@Bean
	public Job generateRecommendBooks(JobRepository jobRepository) {
		return new JobBuilder("generateRecommendBooks", jobRepository)
			.start(generateRecommendBooksStep(jobRepository, transactionManager))
			.build();
	}

	@Bean
	public Step generateRecommendBooksStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("generateRecommendBooksStep", jobRepository)
			.<Long, ChildBooks>chunk(10, transactionManager)
			.reader(childReader())
			.processor(compositeProcessor())
			.writer(consoleItemWriter())
			.build();
	}

	@Bean
	public ItemWriter<ChildBooks> consoleItemWriter() {
		return new ItemWriter<ChildBooks>() {
			@Override
			public void write(Chunk<? extends ChildBooks> chunk) throws Exception {
				chunk.getItems().forEach(bookRecommendBulkRepository::saveAll);
			}
		};
	}

	@Bean
	public CompositeItemProcessor<Long, ChildBooks> compositeProcessor() {
		List<ItemProcessor<?, ?>> processors = new ArrayList<>();
		processors.add(findPreferredRecentBooksProcessor()); // Long -> ChildBooks
		processors.add(findPreferredBooksProcessor()); // ChildBooks -> ChildBooks
		processors.add(findTraitBooksProcessor()); // ChildBooks -> ChildBooks
		processors.add(excludeReadBooksProcessor()); // ChildBooks -> ChildBooks
		processors.add(excludeNotReadRecommendBooksProcessor()); // ChildBooks -> ChildBooks
		processors.add(extractRecommendBooksProcessor()); // ChildBooks -> ChildBooks

		CompositeItemProcessor<Long, ChildBooks> compositeProcessor = new CompositeItemProcessor<>();
		compositeProcessor.setDelegates(processors);
		return compositeProcessor;
	}

	@Bean
	public JdbcCursorItemReader<Long> childReader() {
		JdbcCursorItemReader<Long> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setSql("select id from child;");
		reader.setRowMapper((rs, rowNum) -> rs.getLong("id"));
		return reader;
	}

	@Bean
	public ItemProcessor<Long, ChildBooks> findPreferredRecentBooksProcessor() {
		return child -> {
			// 아이 관심사 조회
			List<String> preferences = childPreferredRepository.findCategoryCodeByChildId(child);
			List<String> booksIsbn;
			// 관심사가 있으면, 관심사 관련 최근 책 2개를 가지고 온다.
			if (preferences.size() > 0) {
				booksIsbn = bookRepository.findIsbnByCategoryCodeSortRecent(preferences);
				return new ChildBooks(child, booksIsbn.isEmpty() ? new ArrayList<>() : booksIsbn);
			}
			// 관심사가 없다면 빈 리스트로 진행
			booksIsbn = new ArrayList<>();
			return new ChildBooks(child, booksIsbn);
		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> findPreferredBooksProcessor() {
		return childBooks -> {
			// 선호하는 카테고리에 맞는 책 조회
			List<String> categoryCodes = childPreferredRepository.findCategoryCodeByChildId(childBooks.childId);
			if (categoryCodes.size() > 0) {
				List<String> preferredBooksIsbn = bookCategoryRepository.findIsbnByCategoryTrait_Id_CategoryCodeIn(categoryCodes);
				childBooks.setTraitBooksIsbn(preferredBooksIsbn);
				return childBooks;
			}
			//선호하는 카테고리가 없을 시
			childBooks.setTraitBooksIsbn(new ArrayList<>());
			return childBooks;
		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> findTraitBooksProcessor() {
		return childBooks -> {
			// 유저 성향 조회
			// TODO : 자녀 성향 공통코드 조회 -> 아직 자녀 성향 누적 테이블이 존재하지 않아 임시 성향을 고정으로 조회
			String[] traitCodes = new String[4];
			traitCodes[0] = "0101_01";
			traitCodes[1] = "0101_03";
			traitCodes[2] = "0101_05";
			traitCodes[3] = "0101_07";
			//선호하는 카테고리가 없을 시 , 전체 책 중에서 성향에 맞는 책을 추천
			if (childBooks.getTraitBooksIsbn().isEmpty()) {
				childBooks.setTraitBooksIsbn(bookCategoryRepository.findIsbnByTraitCodes(traitCodes));
				return childBooks;
			}
			// 선호하는 카테고리 중 성향에 맞는 책을 추천
			List<String> traitBookIsbn = bookCategoryRepository.findIsbnByTraitCodesAndPreferredBooksIn(traitCodes, childBooks.getTraitBooksIsbn());
			if (traitBookIsbn.size() >= needBookCount) {
				childBooks.setTraitBooksIsbn(traitBookIsbn);
			} else {
				// 선호하는 카테고리 중 성향에 맞는 책이 없다면, 전체 책 중에서 성향이 맞는 책을 추천
				childBooks.setTraitBooksIsbn(bookCategoryRepository.findIsbnByTraitCodes(traitCodes));
			}
			if (childBooks.getTraitBooksIsbn().size() < needBookCount) {
				childBooks.setTraitBooksIsbn(new ArrayList<>());
			}
			return childBooks;
		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> excludeReadBooksProcessor() {
		return childBooks -> {
			if (childBooks.getTraitBooksIsbn().size() >= needBookCount) {
				List<String> notReadBooks = bookRepository.findNotReadIsbnByIsbnAndBookView(childBooks.getChildId(), childBooks.getTraitBooksIsbn());
				if (notReadBooks.size() >= needBookCount) {
					childBooks.setTraitBooksIsbn(notReadBooks);
					return childBooks;
				}
			}
			childBooks.getTraitBooksIsbn().clear();
			return childBooks;

		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> excludeNotReadRecommendBooksProcessor() {
		return childBooks -> {
			if (childBooks.getTraitBooksIsbn().size() >= needBookCount) {
				List<String> notReadRecommendBooks = bookRecommendRepository.findByNotReadRecommendBooks(childBooks.getChildId());
				List<String> excludedNotReadRecommendBooks = bookRepository.findIsbnByIsbnAndNotReadRecommendBooks(childBooks.getTraitBooksIsbn(),
					notReadRecommendBooks);
				if (excludedNotReadRecommendBooks.size() >= needBookCount) {
					childBooks.setTraitBooksIsbn(excludedNotReadRecommendBooks);
					return childBooks;
				}
				return childBooks;
			}
			childBooks.getTraitBooksIsbn().clear();
			return childBooks;
		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> extractRecommendBooksProcessor() {
		return childBooks -> {
			if (childBooks.getTraitBooksIsbn().size() >= needBookCount) {
				List<String> recommendBooks = new ArrayList<>();
				recommendBooks.addAll(childBooks.getRecentBooksIsbn());
				List<String> books = bookRepository.findIsbnByBooksInOrderByViewCount(childBooks.getTraitBooksIsbn());
				for (String book : books) {
					if (recommendBooks.size() >= needBookCount) {
						break;
					}
					if (!recommendBooks.contains(book)) {
						recommendBooks.add(book);
					}
				}
				childBooks.setTraitBooksIsbn(recommendBooks);
				return childBooks;
			}
			List<String> recommendBooks = bookRepository.findIsbnByOrderByViewCount();
			childBooks.setTraitBooksIsbn(recommendBooks);
			return childBooks;
		};
	}
}
