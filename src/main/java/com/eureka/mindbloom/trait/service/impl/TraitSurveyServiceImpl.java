package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.trait.domain.ChildTrait;
import com.eureka.mindbloom.trait.domain.survey.TraitAnswer;
import com.eureka.mindbloom.trait.domain.survey.TraitQuestion;
import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;
import com.eureka.mindbloom.trait.dto.response.Answer;
import com.eureka.mindbloom.trait.dto.response.QnAResponse;
import com.eureka.mindbloom.trait.dto.response.TraitPointsResponse;
import com.eureka.mindbloom.trait.repository.TraitAnswerRepository;
import com.eureka.mindbloom.trait.repository.TraitQuestionRepository;
import com.eureka.mindbloom.trait.service.ChildHistoryRecordService;
import com.eureka.mindbloom.trait.service.ChildTraitService;
import com.eureka.mindbloom.trait.service.TraitScoreRecordService;
import com.eureka.mindbloom.trait.service.TraitSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TraitSurveyServiceImpl implements TraitSurveyService {

    private final TraitQuestionRepository questionRepository;
    private final TraitAnswerRepository answerRepository;
    private final ChildRepository childRepository;

    private final TraitScoreRecordService traitScoreRecordService;
    private final ChildHistoryRecordService childHistoryRecordService;
    private final ChildTraitService childTraitService;

    @Override
    @Transactional(readOnly = true)
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
    public void createTraitByQnA(Long childId, List<CreateTraitRequest> answers) {

        Optional<Child> child = childRepository.findById(childId);

        if (child.isEmpty()) {
            throw new NoSuchElementException("자녀 정보가 올바르지 않습니다.");
        }

        // 1. 자녀 별 질의 응답 기록 저장
        Map<Integer, TraitAnswer> responseAnswersMap = childHistoryRecordService.saveChildResponse(child.get(), answers);

        // 2. 임시 child Trait 데이터 저장
        ChildTrait childTrait = childTraitService.partiallySaveChildTrait(child.get());

        // 3. trait record 연산 함수 호출 및 mbti 결과 생성
        List<TraitPointsResponse> traitScores = traitScoreRecordService.saveTraitScoreRecord(childTrait, responseAnswersMap);
        String traitValue = traitScoreRecordService.fetchTraitValue(traitScores);

        // 4. child trait 데이터 저장 마무리
        childTraitService.finishSaveChildTrait(childTrait, traitValue);
    }
}
