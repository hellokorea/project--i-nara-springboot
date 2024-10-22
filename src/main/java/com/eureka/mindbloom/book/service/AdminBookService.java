package com.eureka.mindbloom.book.service;
import com.eureka.mindbloom.book.dto.AdminBookRequestDto;
import com.eureka.mindbloom.book.dto.AdminBookResponseDto;

import java.util.Optional;

public interface AdminBookService {

    AdminBookResponseDto registerBook(AdminBookRequestDto adminBookRequestDto);

    Optional<AdminBookResponseDto> updateBook(String isbn, AdminBookRequestDto adminBookRequestDto);

    void deleteBook(String isbn);


}