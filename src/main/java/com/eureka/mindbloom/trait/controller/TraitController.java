package com.eureka.mindbloom.trait.controller;

import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;
import com.eureka.mindbloom.trait.dto.response.QnAResponse;
import com.eureka.mindbloom.trait.dto.response.TraitHistoryResponse;
import com.eureka.mindbloom.trait.dto.response.TraitValueResultResponse;
import com.eureka.mindbloom.trait.service.ChildRecordHistoryService;
import com.eureka.mindbloom.trait.service.ChildTraitService;
import com.eureka.mindbloom.trait.service.TraitSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/traits/mbti")
public class TraitController {

    private final TraitSurveyService traitSurveyService;
    private final ChildTraitService childTraitService;
    private final ChildRecordHistoryService childRecordHistoryService;

    @GetMapping
    public ApiResponse<List<QnAResponse>> getQnA() {

        List<QnAResponse> qnAResponses = traitSurveyService.getQnA();
        return ApiResponse.success("자녀 MBTI 질문, 답변 리스트 조회 성공", qnAResponses);
    }

    @PostMapping("/{childId}")
    public ApiResponse<String> createTraitByQnA(@PathVariable("childId") Long childId,
                                                @RequestBody List<CreateTraitRequest> answers) {

        traitSurveyService.createTraitByQnA(childId, answers);
        return ApiResponse.success("MBTI 결과가 생성 되었습니다.");
    }

    @GetMapping("/result/{childId}")
    public ApiResponse<TraitValueResultResponse> getChildTraitResult(@PathVariable("childId") Long childId) {

        TraitValueResultResponse traitValueResultResponse = childTraitService.getTraitValueResult(childId);
        return ApiResponse.success("MBTI 검사 결과 입니다.", traitValueResultResponse);
    }

    @GetMapping("/history/{childId}")
    public ApiResponse<List<TraitHistoryResponse>> getChildTraitHistory(@PathVariable("childId") Long childId) {

        List<TraitHistoryResponse> data = childRecordHistoryService.getHistory(childId);
        return ApiResponse.success("MBTI 로그 조회 완료 했습니다.", data);
    }
}
