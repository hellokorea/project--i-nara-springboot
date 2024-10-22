package com.eureka.mindbloom.auth.handler;

import com.eureka.mindbloom.auth.utils.ServletResponseUtil;
import com.eureka.mindbloom.common.dto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ApiResponse<?> success = ApiResponse.success("로그아웃 성공");

        ServletResponseUtil.servletResponse(response, success, HttpStatus.OK);
    }
}