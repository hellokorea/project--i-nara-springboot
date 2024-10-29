package com.eureka.mindbloom.book.util;

import com.eureka.mindbloom.book.dto.AdminBookRequest;
import com.eureka.mindbloom.book.service.AdminBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BookFileProcessor {

    private final AdminBookService adminBookService;

    @Autowired
    public BookFileProcessor(AdminBookService adminBookService) {
        this.adminBookService = adminBookService;
    }

    // 서버의 특정 경로에 있는 CSV 파일을 처리하는 메서드
    public void processCsvFileFromPath(String filePath) throws IOException {
        List<AdminBookRequest> bookRequests = BookCsvImporter.parseCsvFileFromPath(filePath);

        // 파싱된 데이터를 데이터베이스에 저장
        for (AdminBookRequest request : bookRequests) {
            adminBookService.registerBook(request);
        }
    }
}
