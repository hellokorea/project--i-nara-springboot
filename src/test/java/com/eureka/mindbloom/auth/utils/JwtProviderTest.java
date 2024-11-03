package com.eureka.mindbloom.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        String secretKey = "upluseurekaupluseurekaupluseurekaupluseurek";
        jwtProvider = new JwtProvider(secretKey);
    }

    @Nested
    class createToken {

        @Test
        void AccessToken_생성_성공() {
            // given
            String email = "test@gmail.com";
            long expirationTime = 100000;

            // when
            String encodedToken = jwtProvider.createToken(email, expirationTime);

            // then
            assertThat(encodedToken).isNotNull();
            assertThat(encodedToken.split("\\.")).hasSize(3);
        }
    }

    @Nested
    class parseToken {

        @Test
        void 토큰_검증_성공() {
            // given
            String email = "test@gmail.com";
            long expirationTime = 100000;
            String encodedToken = jwtProvider.createToken(email, expirationTime);

            // when
            Jws<Claims> claimsJws = jwtProvider.parseToken(encodedToken);

            // then
            assertThat(claimsJws.getPayload().getSubject()).isEqualTo(email);
            assertThat(claimsJws.getHeader().getAlgorithm()).isEqualTo(Jwts.SIG.HS256.getId());
        }

        @Test
        void 검증_실패_만료된_토큰() {
            // given
            String email = "test@gmail.com";
            long expirationTime = 0;
            String encodedToken = jwtProvider.createToken(email, expirationTime);

            // when & then
            assertThatThrownBy(() -> jwtProvider.parseToken(encodedToken))
                    .isInstanceOf(ExpiredJwtException.class);
        }

        @Test
        void 토큰_검증_실패_bearer_접두사_없는_토큰() {
            // given
            String fakeToken = "fake";

            // when & then
            assertThatThrownBy(() -> jwtProvider.parseToken(fakeToken))
                    .isInstanceOf(BadCredentialsException.class);
        }

        @Test
        void 토큰_검증_실패_유효하지_않은_토큰() {
            // given
            String faceTokenWithBearer = "Bearer fake";

            // when & then
            assertThatThrownBy(() -> jwtProvider.parseToken(faceTokenWithBearer))
                    .isInstanceOf(BadCredentialsException.class);
        }
    }
}