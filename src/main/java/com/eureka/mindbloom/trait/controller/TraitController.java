package com.eureka.mindbloom.trait.controller;

import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;
import com.eureka.mindbloom.trait.dto.response.QnAResponse;
import com.eureka.mindbloom.trait.service.TraitSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/traits/mbti")
public class TraitController {

    private final TraitSurveyService traitSurveyService;

    @GetMapping
    public ApiResponse<List<QnAResponse>> getQnA() {

        List<QnAResponse> qnAResponses = traitSurveyService.getQnA();

        return ApiResponse.success("자녀 MBTI 질문, 답변 리스트 조회 성공", qnAResponses);
    }

    @PostMapping
    public ApiResponse<String> createTraitByQnA(@AuthenticationPrincipal(expression = "member") Member member,
                                                @RequestBody List<CreateTraitRequest> answers) {

        traitSurveyService.createTraitByQnA(member, answers);

        return ApiResponse.success("MBTI 결과가 생성 되었습니다.");
    }
}
