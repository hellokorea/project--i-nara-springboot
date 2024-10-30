package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.book.domain.Book;
import com.eureka.mindbloom.book.dto.BooksResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findBookByIsbn(String isbn);

    // 검색어만 있는 경우
    @Query(value = "SELECT b.* FROM Book b " +
            "WHERE MATCH(b.title, b.author) AGAINST(:search) > 0",
            nativeQuery = true)
    Slice<Book> findByTitleContainingOrAuthorContaining(
            @Param("search") String search,
            Pageable pageable
    );

    // 검색어 조회 by 좋아요순
    @Query(value = "SELECT b.* FROM book b " +
            "JOIN book_like_stats bls ON b.isbn = bls.isbn " +
            "WHERE MATCH(b.title, b.author) AGAINST(:search)",
            nativeQuery = true)
    Slice<Book> findByTitleContainingOrAuthorContainingSortedByLikes(@Param("search") String search, Pageable pageable);

    @Query("SELECT b.isbn FROM Book b JOIN BookCategory bc ON b.isbn = bc.id.isbn WHERE bc.categoryTrait.id.categoryCode IN :preferences ORDER BY b.createdAt DESC LIMIT 2")
	List<String> findIsbnByCategoryCodeSortRecent(List<String> preferences);

	@Query("SELECT b.isbn FROM Book b ORDER BY b.viewCount DESC LIMIT 10")
	List<String> findTop10IsbnByOrderByViewCount();

    // 카테고리 조회
    @Query(value = "SELECT b.* FROM book b " +
            "JOIN book_category bc ON b.isbn = bc.isbn " +
            "JOIN category_trait ct ON bc.category_code = ct.category_code " +
            "AND bc.trait_code = ct.trait_code " +
            "WHERE ct.category_code = :categoryCode",
            nativeQuery = true)
    Slice<Book> findByCategoryCode(@Param("categoryCode") String categoryCode, Pageable pageable);

    // 카테고리 조회 by 좋아요순
    @Query(value = "SELECT b.* FROM book b " +
            "JOIN book_category bc ON b.isbn = bc.isbn " +
            "LEFT JOIN book_like_stats bls ON b.isbn = bls.isbn " +
            "WHERE bc.category_code = :categoryCode",
            nativeQuery = true)
    Slice<Book> findByCategoryCodeSortedByLikes(@Param("categoryCode") String categoryCode, Pageable pageable);

    // 카테고리, 검색어 조회
    @Query(value = "SELECT b.* FROM book b " +
            "JOIN book_category bc ON b.isbn = bc.isbn " +
            "WHERE bc.category_code = :categoryCode " +
            "AND MATCH(b.title, b.author) AGAINST(:search)",
            nativeQuery = true)
    Slice<Book> findByCategoryCodeAndTitleContainingOrAuthorContaining(
            @Param("categoryCode") String categoryCode,
            @Param("search") String search,
            Pageable pageable
    );

    // 카테고리, 검색어 조회 by 좋아요순
    @Query(value = "SELECT b.* FROM book b " +
            "JOIN book_category bc ON b.isbn = bc.isbn " +
            "LEFT JOIN book_like_stats bls ON b.isbn = bls.isbn " +
            "WHERE bc.category_code = :categoryCode " +
            "AND MATCH(b.title, b.author) AGAINST(:search)",
            nativeQuery = true)
    Slice<Book> findByCategoryCodeAndTitleContainingOrAuthorContainingSortedByLikes(
            @Param("categoryCode") String categoryCode,
            @Param("search") String search,
            Pageable pageable
    );

    // 모든 책 조회 with 좋아요수
    @Query(value = "SELECT b.* FROM Book b " +
            "LEFT JOIN book_like_stats bls ON b.isbn = bls.isbn",
            nativeQuery = true)
    Slice<Book> findAllBooksSortedByLikes(Pageable pageable);

    @Query(value = "SELECT b.* FROM Book b ",
            nativeQuery = true)
    Slice<Book> findAllBooks(Pageable pageable);

    Book findByIsbn(String isbn);

    @Query(value = """
            SELECT new com.eureka.mindbloom.book.dto.BooksResponse(b.isbn, b.title, b.author, b.coverImage) FROM Book b
            JOIN BookView bv ON b.isbn = bv.book.isbn
            WHERE bv.child.id = :childId
            ORDER BY bv.id DESC
            """)
    Slice<BooksResponse> findRecentlyReadBook(Pageable pageable, Long childId);
}
