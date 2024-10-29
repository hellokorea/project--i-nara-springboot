package com.eureka.mindbloom.book.Scheduler;

import com.eureka.mindbloom.book.domain.Book;
import com.eureka.mindbloom.book.repository.BookRepository;
import com.eureka.mindbloom.book.service.BookViewCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class BookViewScheduler {

    private final BookRepository bookRepository;
    private final BookViewCacheService bookViewCacheService;

    @Scheduled(fixedRate = 60000)
    public void syncViewCountsToDB(){
        Set<String> keys = bookViewCacheService.getKeys("book:view:*");

        for (String key : keys) {
            String isbn = key.replace("book:view:", "");
            Long viewCount = bookViewCacheService.getViewCount(isbn);

            Book book = bookRepository.findByIsbn(isbn);
            if (book != null) {
                book.addToViewCount(viewCount);
                bookRepository.save(book);
                bookViewCacheService.removeViewCount(isbn);
            }
        }
    }
}
