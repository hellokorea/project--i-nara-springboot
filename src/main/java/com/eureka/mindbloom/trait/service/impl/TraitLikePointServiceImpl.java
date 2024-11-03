package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.book.domain.BookCategory;
import com.eureka.mindbloom.book.repository.BookCategoryRepository;
import com.eureka.mindbloom.book.repository.BookRepository;
import com.eureka.mindbloom.common.exception.NotFoundException;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.trait.service.ChildRecordHistoryService;
import com.eureka.mindbloom.trait.service.TraitLikePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TraitLikePointServiceImpl implements TraitLikePointService {

    private final BookCategoryRepository bookCategoryRepository;
    private final ChildRepository childRepository;
    private final ChildRecordHistoryService childRecordHistoryService;
    private final BookRepository bookRepository; // BookRepository 주입

    private static final String LIKE_CODE = "0300_02";
    private static final int POINT = 1;

    public void processLikePoint(String isbn, Long childId) {
        log.info("Processing Like Point for book ISBN: {}, child: {}", isbn, childId);

        Child child = childRepository.findChildById(childId)
                .orElseThrow(() -> new NotFoundException("Child not found with ID: " + childId));

        BookCategory bookCategory = bookCategoryRepository.findByIsbn(isbn);
        if (bookCategory == null) {
            throw new NotFoundException("BookCategory not found with ISBN: " + isbn);
        }

        String traitCode = bookCategory.getCategoryTrait().getId().getTraitCode();

        // BookRepository를 사용하여 책 제목 조회
        String bookTitle = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Book not found with ISBN: " + isbn))
                .getTitle();


        childRecordHistoryService.createChildTraitHistory(
                child,
                LIKE_CODE,
                traitCode,
                POINT,
                bookTitle
        );
    }
}