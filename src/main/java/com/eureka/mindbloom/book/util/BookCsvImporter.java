package com.eureka.mindbloom.book.util;

import com.eureka.mindbloom.book.dto.AdminBookCategory;
import com.eureka.mindbloom.book.dto.AdminBookRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookCsvImporter {

    // CSV 파일 경로를 받아 AdminBookRequest 객체 리스트로 변환하는 메서드
    public static List<AdminBookRequest> parseCsvFileFromPath(String filePath) throws IOException {
        List<AdminBookRequest> bookRequests = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                AdminBookRequest bookRequest = AdminBookRequest.builder()
                        .isbn(csvRecord.get("isbn"))
                        .title(csvRecord.get("title"))
                        .plot(csvRecord.get("plot"))
                        .author(csvRecord.get("author"))
                        .publisher(csvRecord.get("publisher"))
                        .recommendedAge(csvRecord.get("recommendedAge"))
                        .coverImage(csvRecord.get("coverImage"))
                        .keywords(csvRecord.get("keywords"))
                        .viewCount(Long.valueOf(csvRecord.get("viewCount")))
                        .categories(parseCategories(csvRecord.get("categories"))) // 카테고리 파싱
                        .build();

                bookRequests.add(bookRequest);
            }
        }

        return bookRequests;
    }

    // 카테고리 정보를 파싱하는 메서드
    private static List<AdminBookCategory> parseCategories(String categories) {
        List<AdminBookCategory> categoryList = new ArrayList<>();
        String[] categoryArray = categories.split(",");

        for (String category : categoryArray) {
            categoryList.add(new AdminBookCategory(category.trim()));
        }

        return categoryList;
    }
}
