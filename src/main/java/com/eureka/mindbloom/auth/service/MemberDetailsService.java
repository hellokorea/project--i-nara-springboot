package com.eureka.mindbloom.auth.service;

import com.eureka.mindbloom.auth.dto.MemberDetails;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("존재하지 않는 회원입니다. {%s}", username)));

        return new MemberDetails(member);
    }
}