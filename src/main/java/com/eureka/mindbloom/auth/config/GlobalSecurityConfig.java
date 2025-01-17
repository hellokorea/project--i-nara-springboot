package com.eureka.mindbloom.auth.config;


import com.eureka.mindbloom.auth.filter.JwtAuthorizationFilter;
import com.eureka.mindbloom.auth.filter.JwtLoginFilter;
import com.eureka.mindbloom.auth.handler.JwtLogoutHandler;
import com.eureka.mindbloom.auth.handler.JwtLogoutSuccessHandler;
import com.eureka.mindbloom.auth.service.MemberDetailsService;
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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class GlobalSecurityConfig {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberDetailsService memberDetailsService;

    @Bean
    public AuthenticationProvider memberProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(memberDetailsService);
        provider.setHideUserNotFoundExceptions(false);
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
                        new AntPathRequestMatcher("/health"),
                        new AntPathRequestMatcher("/error"),
                        new AntPathRequestMatcher("/learn"),
                        new AntPathRequestMatcher("/nature"),
                        new AntPathRequestMatcher("/daily-life"),
                        new AntPathRequestMatcher("/stories"),
                        new AntPathRequestMatcher("/animals"),
                        new AntPathRequestMatcher("/search"),
                        new AntPathRequestMatcher("/main"),
                        new AntPathRequestMatcher("/actuator/**"),
                        new AntPathRequestMatcher("/admin"),
                        PathRequest.toStaticResources().atCommonLocations()
                ));

        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/health", "/error").permitAll()
                        .requestMatchers("/signup", "/login", "profile", "/profile/create", "childdetail").permitAll()
                        .requestMatchers(ignoredRequests).permitAll()

                        // Admin 접근 제한
                        .requestMatchers(HttpMethod.POST, "/admin/books/**").hasRole("Admin")
                        .requestMatchers(HttpMethod.PUT, "/admin/books/**").hasRole("Admin")
                        .requestMatchers(HttpMethod.DELETE, "/admin/books/**").hasRole("Admin")
                        .requestMatchers("/admin/**").hasRole("Admin")

                        // 모든 인증된 사용자가 접근할 수 있도록 설정
                        .requestMatchers("/members/children").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.addFilterBefore(new JwtLoginFilter(jwtProvider, memberAuthenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthorizationFilter(jwtProvider, memberDetailsService, ignoredRequests), JwtLoginFilter.class);

        http.logout(config -> config.logoutUrl("/auth/logout")
                .addLogoutHandler(new JwtLogoutHandler())
                .logoutSuccessHandler(new JwtLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .permitAll()
        );

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**", "/templates/**","/js/**", "/images/**", "/html/**", "/favicon.ico", "/index.html", "/login.html", "/adminmain.html");
    }
}
