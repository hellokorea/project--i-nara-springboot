package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.dto.ChildProfileResponse;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.member.service.ChildService;
import com.eureka.mindbloom.trait.domain.history.TraitRecord;
import com.eureka.mindbloom.trait.domain.survey.TraitAnswer;
import com.eureka.mindbloom.trait.domain.survey.TraitQuestion;
import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;
import com.eureka.mindbloom.trait.dto.response.Answer;
import com.eureka.mindbloom.trait.dto.response.QnAResponse;
import com.eureka.mindbloom.trait.repository.ChildTraitRepository;
import com.eureka.mindbloom.trait.repository.TraitAnswerRepository;
import com.eureka.mindbloom.trait.repository.TraitQuestionRepository;
import com.eureka.mindbloom.trait.service.ChildHistoryRecordService;
import com.eureka.mindbloom.trait.service.ChildTraitService;
import com.eureka.mindbloom.trait.service.TraitRecordService;
import com.eureka.mindbloom.trait.service.TraitSurveyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraitSurveyServiceImpl implements TraitSurveyService {

    private final TraitQuestionRepository questionRepository;
    private final TraitAnswerRepository answerRepository;
    private final ChildTraitRepository childTraitRepository;
    private final TraitAnswerRepository traitAnswerRepository;
    private final ChildRepository childRepository;

    private final TraitRecordService traitRecordService;
    private final ChildHistoryRecordService childHistoryRecordService;
    private final ChildTraitService childTraitService;

    private final ChildService childService;

    @Override
    public List<QnAResponse> getQnA() {

        List<TraitQuestion> traitQuestions = questionRepository.findAll();
        List<TraitAnswer> traitAnswers = answerRepository.findAll();

        return traitQuestions.stream()
                .map(q -> {
                    List<Answer> relatedAnswers = traitAnswers.stream()
                            .filter(answer -> answer.getQuestion().getId().equals(q.getId()))
                            .map(a -> Answer.builder()
                                    .answerId(a.getId())
                                    .content(a.getContent())
                                    .build())
                            .collect(Collectors.toList());

                    return QnAResponse.builder()
                            .questionId(q.getId())
                            .content(q.getContent())
                            .choices(relatedAnswers)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createTraitByQnA(Member member, List<CreateTraitRequest> answers) {

        // 일단 테스트 용도
        List<ChildProfileResponse> childes =  childService.getChildProfile(member);
        Optional<Child> child = childRepository.findById(childes.get(0).childId());

        // 1. 자녀 별 질의 응답 기록 저장
        childHistoryRecordService.saveChildResponse(child.get(), answers);

        // 2. 임시 child Trait 데이터 저장

        // 3. trait record 연산 함수 호출 및 mbti 결과 생성
//        calculatorTraitRecordBySurvey(1L, 1L, createTraitRequests);

        // 4 child trait 데이터 저장 마무리
    }

    private void calculatorTraitRecordBySurvey(Long childId, Long childTraitId, List<CreateTraitRequest> createTraitRequests) {

        List<Integer> answerIds = createTraitRequests.stream()
                .map(CreateTraitRequest::getAnswerId)
                .collect(Collectors.toList());

        List<TraitAnswer> traitAnswers = traitAnswerRepository.findAllById(answerIds);

        Map<String, Integer> traitCodeMap = new HashMap<>();

        for (TraitAnswer traitAnswer : traitAnswers) {
            String traitCode = traitAnswer.getTraitCode();
            Integer point = traitAnswer.getPoint();

            traitCodeMap.put(traitCode, traitCodeMap.getOrDefault(traitCode, 0) + point);
        }

        // 별도 분리 처리
        // traitRecordService.saveTraitRecords(childId, childTraitId, traitCodeMap);

        traitCodeMap.forEach((traitCode, traitScore) -> {

            TraitRecord traitRecord = TraitRecord.builder()
//                    .childTrait()
//                    .child()
                    .traitCode(traitCode)
                    .traitScore(traitScore)
                    .build();

        });

    }

/**
 *     childId : 123,
 *     List : [
 *     {
 *        traitCode: 1101_1
 *        score : 80
 *     },
 * ]
 */

    private String calculateMBTITraitValue(List<CreateTraitRequest> createTraitRequests) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}
