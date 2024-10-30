package com.eureka.mindbloom.trait.controller;

import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;
import com.eureka.mindbloom.trait.dto.response.ActionFeedbackResponse;
import com.eureka.mindbloom.trait.dto.response.QnAResponse;
import com.eureka.mindbloom.trait.dto.response.TraitHistoryResponse;
import com.eureka.mindbloom.trait.dto.response.TraitValueResultResponse;
import com.eureka.mindbloom.trait.service.ChildRecordHistoryService;
import com.eureka.mindbloom.trait.service.ChildTraitService;
import com.eureka.mindbloom.trait.service.TraitSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/traits/mbti")
public class TraitController {

    private final TraitSurveyService traitSurveyService;
    private final ChildTraitService childTraitService;
    private final ChildRecordHistoryService childRecordHistoryService;


    @GetMapping("/{childId}")
    public ResponseEntity<ApiResponse<List<QnAResponse>>> getQnA(@PathVariable("childId") Long childId) {

        List<QnAResponse> qnAResponses = traitSurveyService.getQnA(childId);
        return ResponseEntity.ok().body(ApiResponse.success("자녀 MBTI 질문, 답변 리스트 조회 성공", qnAResponses));
    }

    @PostMapping("/{childId}")
    public ResponseEntity<ApiResponse<String>> createTraitByQnA(@PathVariable("childId") Long childId,
                                                                @RequestBody List<CreateTraitRequest> answers) {

        traitSurveyService.createTraitByQnA(childId, answers);
        return ResponseEntity.ok().body(ApiResponse.success("MBTI 결과가 생성 되었습니다."));
    }

    @GetMapping("/result/{childId}")
    public ResponseEntity<ApiResponse<TraitValueResultResponse>> getChildTraitResult(@PathVariable("childId") Long childId) {

        TraitValueResultResponse traitValueResultResponse = childTraitService.getTraitValueResult(childId);
        return ResponseEntity.ok().body(ApiResponse.success("MBTI 검사 결과 조회 완료 했습니다.", traitValueResultResponse));
    }

    @GetMapping("/history/{childId}")
    public ResponseEntity<ApiResponse<TraitHistoryResponse>> getChildTraitHistory(@PathVariable("childId") Long childId) {
        TraitHistoryResponse historyData = childRecordHistoryService.getHistory(childId);
        return ResponseEntity.ok().body(ApiResponse.success("MBTI 로그 조회 완료 했습니다.", historyData));
    }

    // 임시 테스트 컨트롤러 ------------------------
    @PostMapping("/historyTest/{childId}")
    public ApiResponse<ActionFeedbackResponse> testPost(@PathVariable("childId") Long childId) {

        ActionFeedbackResponse data = childRecordHistoryService.testPost(childId);
        return ApiResponse.success("자녀 성향 기록 Insert 테스트", data);
    }

    @DeleteMapping("/{childId}")
    public ResponseEntity<?> deleteTrait(
            @AuthenticationPrincipal(expression = "member") Member member,
            @PathVariable("childId") Long childId) {

        childTraitService.softDeleteChildTraits(member, childId);

        return ResponseEntity.ok().body(ApiResponse.success("OK"));
    }
}
