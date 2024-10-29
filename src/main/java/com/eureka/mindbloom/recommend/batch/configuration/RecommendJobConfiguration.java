package com.eureka.mindbloom.recommend.batch.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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

import com.eureka.mindbloom.book.repository.BookRepository;
import com.eureka.mindbloom.book.repository.BookViewRepository;
import com.eureka.mindbloom.book.repository.jdbc.BookRecommendBulkRepository;
import com.eureka.mindbloom.category.repository.ChildPreferredRepository;
import com.eureka.mindbloom.common.dto.Pair;
import com.eureka.mindbloom.commoncode.domain.CommonCode;
import com.eureka.mindbloom.commoncode.service.CommonCodeConvertService;
import com.eureka.mindbloom.recommend.dto.ChildBooks;
import com.eureka.mindbloom.recommend.repository.BookRecommendRepository;
import com.eureka.mindbloom.recommend.service.RecommendService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RecommendJobConfiguration {

	private final DataSource dataSource;
	private final BookRepository bookRepository;
	private final ChildPreferredRepository childPreferredRepository;
	private final PlatformTransactionManager transactionManager;
	private final BookRecommendRepository bookRecommendRepository;
	private final BookRecommendBulkRepository bookRecommendBulkRepository;
	private final RecommendService recommendService;
	private final BookViewRepository bookViewRepository;
	private final CommonCodeConvertService commonCodeConvertService;

	private final int needBookCount = 10;
	private final int preferredWeight = 3;
	private final int traitWeight = 2;
	private final int similarTraitLikeWeight = 1;

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
			.writer(bookRecommendItemWriter())
			.build();
	}

	@Bean
	public ItemWriter<ChildBooks> bookRecommendItemWriter() {
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
		processors.add(findPreferredRecentBooksProcessor());
		processors.add(findPreferredBooksProcessor());
		processors.add(findTraitBooksProcessor());
		processors.add(findSimilarTraitLikeBooksProcessor());
		processors.add(excludeReadBooksProcessor());
		processors.add(excludeNotReadRecommendBooksProcessor());
		processors.add(extractRecommendBooksProcessor());

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
			List<String> preferencesGroupCode = childPreferredRepository.findCategoryCodeByChildId(child);
			List<String> preferences = new ArrayList<>();
			preferencesGroupCode.forEach(preference -> {
				preferences.addAll(commonCodeConvertService.codeGroupToCommonCodes(preference).stream().map(CommonCode::getCode).toList());
			});

			List<String> booksIsbn;
			if (preferences.size() > 0) {
				booksIsbn = bookRepository.findIsbnByCategoryCodeSortRecent(preferences);
				return new ChildBooks(child, booksIsbn);
			}
			booksIsbn = new ArrayList<>();
			return new ChildBooks(child, booksIsbn);
		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> findPreferredBooksProcessor() {
		return childBooks -> {
			List<String> preferredBooksIsbn = recommendService.getPreferencesBooksByChildId(childBooks.childId);
			if (!preferredBooksIsbn.isEmpty()) {
				Map<String, Integer> traitBooks = childBooks.getTraitBooksIsbn();
				preferredBooksIsbn.stream().forEach(isbn -> traitBooks.put(isbn, preferredWeight));
			}
			return childBooks;
		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> findTraitBooksProcessor() {
		return childBooks -> {
			List<String> traitBooks = recommendService.getTraitBooksByChildId(childBooks.childId);
			if (!traitBooks.isEmpty()) {
				Map<String, Integer> traitBooksIsbn = childBooks.getTraitBooksIsbn();
				traitBooks.stream().forEach(isbn -> traitBooksIsbn.put(isbn, traitBooksIsbn.getOrDefault(isbn, 0) + traitWeight));
			}
			return childBooks;
		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> findSimilarTraitLikeBooksProcessor() {
		return childBooks -> {
			List<String> similarTraitBooks = recommendService.getSimilarTraitLikeBooks(childBooks.childId);
			if (!similarTraitBooks.isEmpty()) {
				Map<String, Integer> traitBooksIsbn = childBooks.getTraitBooksIsbn();
				similarTraitBooks.stream().forEach(isbn -> traitBooksIsbn.put(isbn, traitBooksIsbn.getOrDefault(isbn, 0) + similarTraitLikeWeight));
			}
			return childBooks;
		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> excludeReadBooksProcessor() {
		return childBooks -> {
			if (childBooks.getTraitBooksIsbn().size() >= needBookCount) {
				List<String> readBooks = bookViewRepository.findReadIsbnByChildId(childBooks.getChildId());
				Map<String, Integer> traitBooksIsbn = childBooks.getTraitBooksIsbn();
				traitBooksIsbn.keySet().removeAll(readBooks);
			}
			return childBooks;

		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> excludeNotReadRecommendBooksProcessor() {
		return childBooks -> {
			if (childBooks.getTraitBooksIsbn().size() >= needBookCount) {
				List<String> notReadRecommendBooks = bookRecommendRepository.findByNotReadRecommendBooks(childBooks.getChildId());
				Map<String, Integer> traitBooksIsbn = childBooks.getTraitBooksIsbn();
				if (traitBooksIsbn.size() - notReadRecommendBooks.size() >= needBookCount) {
					traitBooksIsbn.keySet().removeAll(notReadRecommendBooks);
				}

			}
			return childBooks;
		};
	}

	@Bean
	public ItemProcessor<ChildBooks, ChildBooks> extractRecommendBooksProcessor() {
		return childBooks -> {
			if (childBooks.getTraitBooksIsbn().size() >= needBookCount) {
				List<String> recommendBooks = childBooks.getRecommendBooksIsbn();
				PriorityQueue<Pair<String, Integer>> priorityQueue = new PriorityQueue<>((first, second) -> second.getB() - first.getB());
				priorityQueue.addAll(childBooks.getTraitBooksIsbn().entrySet().stream().map(entry -> new Pair<>(entry.getKey(), entry.getValue())).toList());
				while (recommendBooks.size() < needBookCount) {
					recommendBooks.add(priorityQueue.poll().getA());
				}
				return childBooks;
			}
			childBooks.setRecommendBooksIsbn(recommendService.getTopViewedBooks());
			return childBooks;
		};
	}
}
