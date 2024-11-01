package com.eureka.mindbloom.book.service.Impl;

import com.eureka.mindbloom.book.domain.Book;
import com.eureka.mindbloom.book.domain.BookLikeStats;
import com.eureka.mindbloom.book.domain.BookView;
import com.eureka.mindbloom.book.dto.*;
import com.eureka.mindbloom.book.repository.BookCategoryRepository;
import com.eureka.mindbloom.book.repository.BookLikeStatsRepository;
import com.eureka.mindbloom.book.repository.BookRepository;
import com.eureka.mindbloom.book.repository.BookViewRepository;
import com.eureka.mindbloom.book.service.BookService;
import com.eureka.mindbloom.book.service.BookViewCacheService;
import com.eureka.mindbloom.book.type.SortOption;
import com.eureka.mindbloom.common.exception.NotFoundException;
import com.eureka.mindbloom.commoncode.service.CommonCodeConvertService;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.exception.ChildNotFoundException;
import com.eureka.mindbloom.member.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookLikeStatsRepository bookLikeStatsRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final CommonCodeConvertService commonCodeConvertService;
    private final BookViewRepository bookViewRepository;
    private final ChildRepository childRepository;
    private final BookViewCacheService bookViewCacheService;

    @Override
    public BookListResponse getBooks(String categoryCode, String search, int page, SortOption sortOption) {
        if (sortOption == null) {
            sortOption = SortOption.RELEVANCE;
        }

        // 정렬 기준 설정
        Sort sort = switch (sortOption) {
            case VIEWCOUNT -> Sort.by("view_count").descending(); // 조회수 높은순
            case LIKES -> Sort.by("bls.count").descending(); // 좋아요 많은순
            case RECENT -> Sort.by("created_at").descending(); // 최신순
            case RELEVANCE -> Sort.unsorted(); // 정확도 기준
        };

        int pageSize = 10; // 페이지 당 보여주는 책의 수
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Slice<Book> books;
        if (categoryCode != null && search != null) {
            // 카테고리와 검색어가 둘 다 있는 경우
            books = (sortOption == SortOption.LIKES)
                    ? bookRepository.findByCategoryCodeAndTitleContainingOrAuthorContainingSortedByLikes(categoryCode, search, pageable)
                    : bookRepository.findByCategoryCodeAndTitleContainingOrAuthorContaining(categoryCode, search, pageable);
        } else if (categoryCode != null) {
            // 카테고리만 있는 경우
            books = (sortOption == SortOption.LIKES)
                    ? bookRepository.findByCategoryCodeSortedByLikes(categoryCode, pageable)
                    : bookRepository.findByCategoryCode(categoryCode, pageable);
        } else if (search != null) {
            // 검색어만 있는 경우
            books = (sortOption == SortOption.LIKES)
                    ? bookRepository.findByTitleContainingOrAuthorContainingSortedByLikes(search, pageable)
                    : bookRepository.findByTitleContainingOrAuthorContaining(search, pageable);
        } else {
            // 아무 조건도 없을 때
            books = (sortOption == SortOption.LIKES)
                    ? bookRepository.findAllBooksSortedByLikes(pageable)
                    : bookRepository.findAllBooks(pageable);
        }

        // Book -> BooksResponse로 변환
        List<BooksResponse> bookResponses = books.getContent().stream()
                .map(book -> new BooksResponse(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getCoverImage()))
                .collect(Collectors.toList());

        return new BookListResponse(bookResponses, books.isLast());
    }

    @Override
    public BookListResponse getRecentlyViewedBooks(int page, Long childId, Member member) {
        if (isNotParent(member, childId)) {
            throw new ChildNotFoundException(childId);
        }

        Pageable pageable = PageRequest.of(page, 10);

        Slice<BooksResponse> books = bookRepository.findRecentlyReadBook(pageable, childId);

        return new BookListResponse(books.getContent(), books.isLast());
    }

    private boolean isNotParent(Member member, Long childId) {
        return member.getChildren().stream().noneMatch(child -> child.getId().equals(childId));
    }

    @Override
    public BookDetailResponse getBookDetail(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);

        String categoryCode = bookCategoryRepository.findByIsbn(isbn).getCategoryCode();

        String categoryName = commonCodeConvertService.codeToCommonCodeName(categoryCode);

        List<BookLikeStats> likeStatsList = bookLikeStatsRepository.likeCountByIsbn(isbn);
        Long likeCount = likeStatsList.stream()
                .filter(stats -> "0300_02".equals(stats.getId().getType()))
                .map(BookLikeStats::getCount)
                .findFirst()
                .orElse(0L); // 없으면 0으로 초기화

        return new BookDetailResponse(
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPlot(),
                book.getPublisher(),
                book.getRecommendedAge(),
                book.getCoverImage(),
                categoryName,
                book.getKeywords(),
                book.getViewCount(),
                likeCount,
                book.getCreatedAt()
        );
    }

    @Override
    public ReadBookResponse readBook(String isbn, Long childId, Member member) {
        Book book = bookRepository.findByIsbn(isbn);
        Child child = childRepository.findChildById(childId)
                .orElseThrow(() -> NotFoundException.childNotFound(childId));

        if (isNotParent(member, childId)) {
            throw new ChildNotFoundException(childId);
        }

        boolean viewExists = bookViewRepository.existsByBookAndChild(book, child);
        if (viewExists) {
            return new ReadBookResponse(
                    book.getIsbn(),
                    book.getTitle()
            );
        }
        bookViewCacheService.incrementViewCount(isbn);

        BookView bookView = new BookView(book, child);
        bookViewRepository.save(bookView);

        return new ReadBookResponse(
                book.getIsbn(),
                book.getTitle()
        );
    }

    @Override
    public List<SimilarBookResponse> getBooksByCategory(String isbn, int limit) {
        String categoryCode = bookRepository.findCategoryCodeByIsbn(isbn);
        if (categoryCode == null) {
            return Collections.emptyList();
        }
        Pageable pageable = PageRequest.of(0, limit);
        return bookRepository.findBooksByCategoryCode(categoryCode, isbn, pageable);
    }
}
