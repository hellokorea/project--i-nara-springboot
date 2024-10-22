package com.eureka.mindbloom.trait.controller;

import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.trait.dto.request.AdminQnARequest;
import com.eureka.mindbloom.trait.dto.response.AdminQnADeleteResponse;
import com.eureka.mindbloom.trait.dto.response.AdminQnAResponse;
import com.eureka.mindbloom.trait.service.AdminTraitSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/traits/mbti")
public class AdminTraitSurveyController {

    private final AdminTraitSurveyService adminTraitSurveyService;

    @GetMapping
    public ApiResponse<List<AdminQnAResponse>> getQnA() {

        List<AdminQnAResponse> getQnAData = adminTraitSurveyService.getQnA();

        return ApiResponse.success("모든 질문, 답변 리스트를 성공적으로 조회 했습니다.", getQnAData);
    }

    @PostMapping
    public ApiResponse<AdminQnAResponse> createQnA(@RequestBody AdminQnARequest adminQnARequest) {

        AdminQnAResponse createQnAData = adminTraitSurveyService.createQnA(adminQnARequest);

        return ApiResponse.success("질문, 답변이 성공적으로 저장 되었습니다.", createQnAData);
    }

    @PutMapping("/{questionId}")
    public ApiResponse<AdminQnAResponse> updateQnA(@PathVariable Integer questionId, @RequestBody AdminQnARequest updateQnADto) {

        AdminQnAResponse updateQnAData = adminTraitSurveyService.updateQnA(questionId, updateQnADto);

        return ApiResponse.success("질문, 답변이 성공적으로 수정 되었습니다.", updateQnAData);
    }

    @DeleteMapping("/{questionId}")
    public ApiResponse<AdminQnADeleteResponse> DeleteQnA(@PathVariable Integer questionId) {

        AdminQnADeleteResponse deleteQnAData = adminTraitSurveyService.deleteQnA(questionId);

        return ApiResponse.success("질문, 답변이 성공적으로 삭제 되었습니다.", deleteQnAData);
    }
}
