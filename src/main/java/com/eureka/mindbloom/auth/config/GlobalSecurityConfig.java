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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import java.util.List;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize를 사용하기 위한 설정
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
                        new AntPathRequestMatcher("/main")
                ));

        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/health", "/error").permitAll()
                        .requestMatchers("/signup", "/login").permitAll()  // signup & login 페이지 접근 권한 허용
                        .requestMatchers(ignoredRequests).permitAll()
                        .requestMatchers(HttpMethod.POST, "/admin/books/**").hasRole("Admin")  // Admin 접근 설정 추가
                        .requestMatchers(HttpMethod.PUT, "/admin/books/**").hasRole("Admin")   // Admin 접근 설정 추가
                        .requestMatchers(HttpMethod.DELETE, "/admin/books/**").hasRole("Admin") // Admin 접근 설정 추가
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
                .requestMatchers("/css/**", "/js/**", "/images/**", "/html/**", "/favicon.ico", "/index.html");
    }
}
