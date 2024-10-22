package com.eureka.mindbloom.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Objects;

public class JwtLogoutHandler extends SecurityContextLogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);

        if (Objects.nonNull(response.getHeader("Authorization"))) {
            response.setHeader("Authorization", null);
        }
    }
}