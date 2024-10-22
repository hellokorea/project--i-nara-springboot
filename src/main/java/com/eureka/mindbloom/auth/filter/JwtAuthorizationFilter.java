package com.eureka.mindbloom.auth.filter;

import com.eureka.mindbloom.auth.config.MemberDetails;
import com.eureka.mindbloom.auth.config.MemberDetailsService;
import com.eureka.mindbloom.auth.utils.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberDetailsService detailsService;
    private final RequestMatcher matchers;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if (matchers.matches(req)) {
            filterChain.doFilter(req, res);
            return;
        }

        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        Jws<Claims> decodedToken = jwtProvider.parseToken(token);

        String subject = decodedToken.getPayload().getSubject();
        MemberDetails userDetails = detailsService.loadUserByUsername(subject);
        setAuthentication(userDetails);

        filterChain.doFilter(req, res);
    }

    private void setAuthentication(UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}