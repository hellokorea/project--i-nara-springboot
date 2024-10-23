package com.eureka.mindbloom.member.service;

import com.eureka.mindbloom.member.dto.SignUpRequest;
import com.eureka.mindbloom.member.exception.DuplicationEmailException;
import com.eureka.mindbloom.member.repository.MemberRepository;
import com.eureka.mindbloom.member.service.Impl.MemberServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import com.eureka.mindbloom.member.dto.SignUpResponse;
import com.eureka.mindbloom.member.domain.Member;

@ExtendWith(MockitoExtension.class)
public class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    private SignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123@")
                .name("Test User")
                .build();
    }

    @Nested
    class signUp {

        @Test
        void 회원가입_성공() {
            // given
            Member member = Member.builder()
                    .name(signUpRequest.getName())
                    .email(signUpRequest.getEmail())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .role("0400_02")
                    .build();

            when(memberRepository.save(any(Member.class))).thenReturn(member);

            // when
            SignUpResponse response = memberService.signUp(signUpRequest);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getEmail()).isEqualTo(signUpRequest.getEmail());
            verify(memberRepository).save(any(Member.class));
        }

        @Test
        void 회원가입_실패_이메일_중복() {
            // given
            when(memberRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> memberService.signUp(signUpRequest))
                    .isInstanceOf(DuplicationEmailException.class)
                    .hasMessage("이미 존재하는 이메일입니다: " + signUpRequest.getEmail());
        }
    }
}