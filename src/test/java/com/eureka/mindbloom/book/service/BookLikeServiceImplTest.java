package com.eureka.mindbloom.book.service;

import com.eureka.mindbloom.book.domain.*;
import com.eureka.mindbloom.book.dto.BookLikeStatsResponse;
import com.eureka.mindbloom.book.exception.DuplicateLikeException;
import com.eureka.mindbloom.book.repository.BookLikeRepository;
import com.eureka.mindbloom.book.repository.BookLikeStatsRepository;
import com.eureka.mindbloom.book.repository.BookRepository;
import com.eureka.mindbloom.book.service.Impl.BookLikeServiceImpl;
import com.eureka.mindbloom.common.exception.NotFoundException;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.repository.ChildRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookLikeServiceImplTest {

    @Mock
    private BookLikeRepository bookLikeRepository;

    @Mock
    private BookLikeStatsRepository bookLikeStatsRepository;

    @Mock
    private ChildRepository childRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private BookLikeServiceImpl bookLikeService;

    private static final String TEST_ISBN = "9788956746425";
    private static final Long TEST_CHILD_ID = 1L;
    private static final String TEST_LIKE_TYPE = "0300_02";

    private Book book;
    private Child child;
    private BookLike bookLike;
    private BookLikeStats bookLikeStats;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .isbn(TEST_ISBN)
                .title("Test Book")
                .build();

        child = Child.builder()
                .name("Test Child")
                .build();

        bookLike = BookLike.builder()
                .type(TEST_LIKE_TYPE)
                .book(book)
                .child(child)
                .build();

        bookLikeStats = BookLikeStats.builder()
                .book(book)
                .type(TEST_LIKE_TYPE)
                .count(1L)
                .build();
    }

    @Nested
    class AddLike{

        @Test
        void 좋아요_추가_성공(){
            // given
            when(bookRepository.findBookByIsbn(TEST_ISBN)).thenReturn(Optional.of(book));
            when(childRepository.findChildById(TEST_CHILD_ID)).thenReturn(Optional.of(child));
            when(bookLikeRepository.findById(any(BookChildId.class))).thenReturn(Optional.empty());
            when(bookLikeRepository.save(any(BookLike.class))).thenReturn(bookLike);
            when(bookLikeStatsRepository.findById(any(BookLikeStatsId.class))).thenReturn(Optional.of(bookLikeStats));

            // when
            BookLike result = bookLikeService.addLike(TEST_ISBN, TEST_CHILD_ID, TEST_LIKE_TYPE);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getBook().getIsbn()).isEqualTo(TEST_ISBN);
            assertThat(result.getChild().getId()).isEqualTo(child.getId());
            verify(bookLikeRepository).save(any(BookLike.class));
            verify(bookLikeStatsRepository).save(any(BookLikeStats.class));
        }

        @Test
        @DisplayName("책을 찾을 수 없음")
        void 좋아요_추가_실패_1(){
            // given
            when(bookRepository.findBookByIsbn(TEST_ISBN)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> bookLikeService.addLike(TEST_ISBN, TEST_CHILD_ID, TEST_LIKE_TYPE))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("이미 존재하는 좋아요")
        void 좋아요_추가_실패_2(){
            // given
            when(bookRepository.findBookByIsbn(TEST_ISBN)).thenReturn(Optional.of(book));
            when(childRepository.findChildById(TEST_CHILD_ID)).thenReturn(Optional.of(child));
            when(bookLikeRepository.findById(any(BookChildId.class))).thenReturn(Optional.of(bookLike));

            // when & then
            assertThatThrownBy(() -> bookLikeService.addLike(TEST_ISBN, TEST_CHILD_ID, TEST_LIKE_TYPE))
                    .isInstanceOf(DuplicateLikeException.class);

        }
    }

    @Nested
    class RemoveLike{

        @Test
        void 좋아요_제거_성공(){
            // given
            when(bookLikeRepository.findById(any(BookChildId.class))).thenReturn(Optional.of(bookLike));
            when(bookLikeStatsRepository.findById(any(BookLikeStatsId.class))).thenReturn(Optional.of(bookLikeStats));

            // when
            bookLikeService.removeLike(TEST_ISBN, TEST_CHILD_ID);

            // then
            verify(bookLikeRepository).delete(any(BookLike.class));
            verify(bookLikeStatsRepository).save(any(BookLikeStats.class));

        }

        @Test
        void 좋아요_제거_실패(){
            // given
            when(bookLikeRepository.findById(any(BookChildId.class))).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> bookLikeService.removeLike(TEST_ISBN, TEST_CHILD_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class GetBookLikeStats{

        @Test
        @DisplayName("좋아요 통계 조회 성공")
        void getBookLikeStats_Success() {
            // given
            List<BookLikeStats> statsList = List.of(bookLikeStats);
            when(bookLikeStatsRepository.likeCountByIsbn(TEST_ISBN)).thenReturn(statsList);

            // when
            List<BookLikeStatsResponse> result = bookLikeService.getBookLikeStats(TEST_ISBN);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getIsbn()).isEqualTo(TEST_ISBN);
            assertThat(result.get(0).getCount()).isEqualTo(1L);
            verify(bookLikeStatsRepository).likeCountByIsbn(TEST_ISBN);
        }

    }
}
