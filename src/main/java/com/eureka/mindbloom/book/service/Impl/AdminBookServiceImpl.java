package com.eureka.mindbloom.book.service.Impl;

import com.eureka.mindbloom.book.domain.Book;
import com.eureka.mindbloom.book.domain.BookCategory;
import com.eureka.mindbloom.book.dto.AdminBookCategory;
import com.eureka.mindbloom.book.dto.AdminBookRequest;
import com.eureka.mindbloom.book.dto.AdminBookResponse;
import com.eureka.mindbloom.book.repository.BookCategoryRepository;
import com.eureka.mindbloom.book.repository.BookRepository;
import com.eureka.mindbloom.book.service.AdminBookService;
import com.eureka.mindbloom.book.service.ChatGPTService;
import com.eureka.mindbloom.category.domain.CategoryTrait;
import com.eureka.mindbloom.category.repository.CategoryTraitRepository;
import com.eureka.mindbloom.commoncode.domain.CommonCode;
import com.eureka.mindbloom.commoncode.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminBookServiceImpl implements AdminBookService {

    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final CategoryTraitRepository categoryTraitRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final ChatGPTService chatGPTService;

    @Override
    @Transactional
    public AdminBookResponse registerBook(AdminBookRequest dto) {
        // 도서 등록
        Book book = dto.toEntity();

        // plot 기반 MBTI 예측 및 설정
        String predictedMBTI = chatGPTService.predictMBTI(book.getPlot());
        book.setKeywords(predictedMBTI);

        bookRepository.save(book);

        // 카테고리와 관련된 정보 등록
        List<String> categoryNames = new ArrayList<>();
        List<String> traitNames = new ArrayList<>();

        saveBookCategories(dto.getCategories(), book, categoryNames, traitNames);

        return new AdminBookResponse(book, categoryNames, traitNames);
    }

    @Override
    @Transactional
    public List<AdminBookResponse> bulkRegisterBooks(List<AdminBookRequest> bookRequests) {
        List<AdminBookResponse> responses = new ArrayList<>();

        for (AdminBookRequest request : bookRequests) {
            responses.add(registerBook(request));
        }

        return responses;
    }

    // 카테고리 저장 로직에 traitName 포함
    private void saveBookCategories(List<AdminBookCategory> categories, Book book, List<String> categoryNames, List<String> traitNames) {
        for (AdminBookCategory categoryDto : categories) {
            // CommonCode에서 categoryCode를 찾음
            CommonCode commonCode = commonCodeRepository.findByName(categoryDto.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 이름입니다."));

            // CategoryTrait 찾기 (List로 반환됨)
            List<CategoryTrait> categoryTraits = categoryTraitRepository.findByIdCategoryCode(commonCode.getCode());
            if (categoryTraits.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 카테고리 코드입니다.");
            }
            CategoryTrait categoryTrait = categoryTraits.get(0); // 첫 번째 결과만 사용

            // 카테고리 이름과 trait 저장
            categoryNames.add(categoryDto.getCategory());
            traitNames.add(getTraitName(categoryTrait)); // traitName 추가

            BookCategory bookCategory = new BookCategory(categoryTrait, book);
            bookCategoryRepository.save(bookCategory);
        }
    }

    // CategoryTrait의 traitName을 가져오는 메서드
    private String getTraitName(CategoryTrait categoryTrait) {
        // traitCode로 CommonCode 검색
        CommonCode commonCode = commonCodeRepository.findByCode(categoryTrait.getId().getTraitCode())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 trait 코드입니다."));

        return commonCode.getName(); // traitName 반환
    }

    @Override
    @Transactional
    public Optional<AdminBookResponse> updateBook(String isbn, AdminBookRequest dto) {
        return bookRepository.findById(isbn).map(existingBook -> {
            // 도서 정보 업데이트
            existingBook.updateDetails(dto.toEntity());

            // plot 기반 MBTI 예측 및 설정
            String predictedMBTI = chatGPTService.predictMBTI(existingBook.getPlot());
            existingBook.setKeywords(predictedMBTI);

            // 기존 카테고리 삭제 후 다시 추가
            bookCategoryRepository.deleteByBook(existingBook); // 기존 카테고리 삭제
            List<String> categoryNames = new ArrayList<>();
            List<String> traitNames = new ArrayList<>();

            saveBookCategories(dto.getCategories(), existingBook, categoryNames, traitNames); // 카테고리와 관련 정보 저장

            return new AdminBookResponse(existingBook, categoryNames, traitNames);
        });
    }

    @Override
    @Transactional
    public void deleteBook(String isbn) {
        // 관련된 book_category 레코드 먼저 삭제
        bookCategoryRepository.deleteByBookIsbn(isbn);

        // 그 후 book 레코드 삭제
        bookRepository.deleteById(isbn);
    }
}
