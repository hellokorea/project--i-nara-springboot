package com.eureka.mindbloom.book.service;

import com.eureka.mindbloom.book.dto.AdminBookRequest;
import com.eureka.mindbloom.book.dto.AdminBookResponse;

import java.util.Optional;

public interface AdminBookService {

    AdminBookResponse registerBook(AdminBookRequest dto);

    Optional<AdminBookResponse> updateBook(String isbn, AdminBookRequest dto);

    void deleteBook(String isbn);
}
