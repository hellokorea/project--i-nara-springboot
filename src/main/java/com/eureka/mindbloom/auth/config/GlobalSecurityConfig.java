package com.eureka.mindbloom.auth.config;

import com.eureka.mindbloom.auth.filter.JwtAuthorizationFilter;
import com.eureka.mindbloom.auth.filter.JwtLoginFilter;
import com.eureka.mindbloom.auth.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class GlobalSecurityConfig {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberDetailsService memberDetailsService;

    @Bean
    public AuthenticationProvider memberProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(memberDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager memberAuthenticationManager() {
        return new ProviderManager(memberProvider());
    }

    @Bean
    public SecurityFilterChain globalSecurityFilterChain(HttpSecurity http) throws Exception {
        final RequestMatcher ignoredRequests = new OrRequestMatcher(
                List.of(new AntPathRequestMatcher("/members/signup", HttpMethod.POST.name()),
                        new AntPathRequestMatcher("/auth/login", HttpMethod.POST.name()),
                        new AntPathRequestMatcher("/auth/logout", HttpMethod.POST.name()),
                        new AntPathRequestMatcher("/health")
                ));

        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(common -> common
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(HttpMethod.GET, "/health", "/error").permitAll()
                .anyRequest().permitAll());

        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.addFilterBefore(new JwtLoginFilter(jwtProvider, memberAuthenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthorizationFilter(jwtProvider, memberDetailsService, ignoredRequests), JwtLoginFilter.class);

        return http.build();
    }
}
