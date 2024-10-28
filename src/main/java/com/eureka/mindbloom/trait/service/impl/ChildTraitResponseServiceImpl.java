package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.common.exception.BaseException;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.domain.ChildTrait;
import com.eureka.mindbloom.trait.domain.survey.ChildTraitResponses;
import com.eureka.mindbloom.trait.domain.survey.TraitAnswer;
import com.eureka.mindbloom.trait.domain.survey.TraitQuestion;
import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;
import com.eureka.mindbloom.trait.repository.ChildTraitRepository;
import com.eureka.mindbloom.trait.repository.ChildTraitResponseRepository;
import com.eureka.mindbloom.trait.repository.TraitAnswerRepository;
import com.eureka.mindbloom.trait.repository.TraitQuestionRepository;
import com.eureka.mindbloom.trait.service.ChildTraitResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChildTraitResponseServiceImpl implements ChildTraitResponseService {

    private final ChildTraitResponseRepository childTraitResponseRepository;
    private final TraitQuestionRepository traitQuestionRepository;
    private final TraitAnswerRepository traitAnswerRepository;
    private final ChildTraitRepository childTraitRepository;

    @Override
    public Map<Integer, TraitAnswer> saveChildResponse(Child child, List<CreateTraitRequest> answers) {

        Pageable pageable = PageRequest.of(0, 1);
        List<ChildTrait> childTraits = childTraitRepository.findChildTraitByDeletedAtIsNull(child.getId(), pageable);

        if (!childTraits.isEmpty()) {
            throw new BaseException("해당 자녀는 이미 MBTI 를 가지고 있습니다.", HttpStatus.CONFLICT);
        }

        Map<Integer, TraitQuestion> questionMap = fetchQuestionMap(answers);
        Map<Integer, TraitAnswer> answerMap = fetchAnswerMap(answers);
//        Map<Integer, ChildTraitResponses> childTraitResponsesMap = fetchChildResponseMap(child.getId(), questionMap);

        List<ChildTraitResponses> responses = answers.stream()
                .map(traitData -> {

                    TraitQuestion question = questionMap.get(traitData.getQuestionId());
                    TraitAnswer answer = answerMap.get(traitData.getAnswerId());

                    // 업데이트 처리
//                   ChildTraitResponses exResponses = childTraitResponsesMap.get(traitData.getQuestionId());
//                    if (exResponses != null) {
//                        exResponses.updateAnswer(answer);
//                        return exResponses;
//                    } else {
//                    }

                    return ChildTraitResponses.builder()
                            .child(child)
                            .question(question)
                            .answer(answer)
                            .build();
                })
                .collect(Collectors.toList());

        childTraitResponseRepository.saveAll(responses);

        return answerMap;
    }

    private Map<Integer, TraitQuestion> fetchQuestionMap(List<CreateTraitRequest> answers) {
        List<Integer> questionIds = answers.stream()
                .map(CreateTraitRequest::getQuestionId)
                .collect(Collectors.toList());

        validateQuestions(questionIds);

        return traitQuestionRepository.findAllById(questionIds)
                .stream()
                .collect(Collectors.toMap(TraitQuestion::getId, question -> question));
    }

    private Map<Integer, TraitAnswer> fetchAnswerMap(List<CreateTraitRequest> answers) {
        List<Integer> answerIds = answers.stream()
                .map(CreateTraitRequest::getAnswerId)
                .collect(Collectors.toList());

        validateAnswers(answerIds);

        return traitAnswerRepository.findAllById(answerIds)
                .stream()
                .collect(Collectors.toMap(TraitAnswer::getId, answer -> answer));
    }

    private Map<Integer, ChildTraitResponses> fetchChildResponseMap(Long childId, Map<Integer, TraitQuestion> questions) {
        List<Integer> questionIds = new ArrayList<>(questions.keySet());

        List<ChildTraitResponses> existingResponse = childTraitResponseRepository
                .findByChildIdAndQuestionIds(childId, questionIds);

        return existingResponse.stream()
                .collect(Collectors.toMap(response -> response.getQuestion().getId(), response -> response));
    }

    private void validateQuestions(List<Integer> Ids) {
        List<TraitQuestion> questions = traitQuestionRepository.findAllById(Ids);

        if (questions.size() != Ids.size()) {
            throw new BaseException("모든 질문에 대한 답변을 해야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateAnswers(List<Integer> Ids) {
        List<TraitAnswer> answers = traitAnswerRepository.findAllById(Ids);

        if (answers.size() != Ids.size()) {
            throw new BaseException("모든 질문에 대한 답변을 해야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
