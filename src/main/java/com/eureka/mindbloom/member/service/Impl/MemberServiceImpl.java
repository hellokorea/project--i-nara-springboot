package com.eureka.mindbloom.member.service.Impl;

import com.eureka.mindbloom.common.exception.DuplicationEmailException;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.dto.SignUpRequest;
import com.eureka.mindbloom.member.dto.SignUpResponse;
import com.eureka.mindbloom.member.dto.UpdateMemberProfileRequest;
import com.eureka.mindbloom.member.repository.MemberRepository;
import com.eureka.mindbloom.member.service.GetMemberProfileResponse;
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
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicationEmailException("이미 존재하는 이메일 주소입니다.");
        }

        Member savedMember = createMemberFromDto(signUpRequest);
        savedMember = memberRepository.save(savedMember);

        return SignUpResponse.builder()
                .memberId(savedMember.getId())
                .name(savedMember.getName())
                .email(savedMember.getEmail())
                .build();
    }

    @Override
    public GetMemberProfileResponse getMemberProfile(Member member) {
        return GetMemberProfileResponse.from(member);
    }

    @Override
    public void updateMemberProfile(Member member, UpdateMemberProfileRequest request) {
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.updateMember(request.name(), encodedPassword, request.phone());

        memberRepository.save(member);
    }

    private Member createMemberFromDto(SignUpRequest request) {
        return Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("0400_02") // 기본 역할 설정
                .build();
    }
}
