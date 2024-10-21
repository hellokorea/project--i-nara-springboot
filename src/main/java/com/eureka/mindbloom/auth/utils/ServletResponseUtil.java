package com.eureka.mindbloom.auth.utils;

import com.eureka.mindbloom.common.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServletResponseUtil {
    public static void servletResponse(HttpServletResponse response,
                                       ApiResponse<?> responseDto,
                                       HttpStatus status) throws IOException {

        String jsonResponse = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(responseDto);

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(jsonResponse);
    }
}