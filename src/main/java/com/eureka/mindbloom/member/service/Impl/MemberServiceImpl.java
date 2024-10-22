package com.eureka.mindbloom.member.service.Impl;

import com.eureka.mindbloom.member.dto.SignUpResponse;
import com.eureka.mindbloom.member.dto.SignUpRequest;
import com.eureka.mindbloom.auth.exception.DuplicationEmailException;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.repository.MemberRepository;
import com.eureka.mindbloom.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequestDto) {
        if(memberRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new DuplicationEmailException("이미 존재하는 이메일 주소입니다.");
        }

        Member savedMember = createMemberFromDto(signUpRequestDto);
        savedMember = memberRepository.save(savedMember);

        return SignUpResponse.builder()
                .memberId(savedMember.getId())
                .name(savedMember.getName())
                .email(savedMember.getEmail())
                .build();
    }

    private Member createMemberFromDto(SignUpRequest dto) {
        return Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("0400_02") // 기본 역할 설정
                .build();
    }
}
