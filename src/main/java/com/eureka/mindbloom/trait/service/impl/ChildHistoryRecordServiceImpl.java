package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.domain.survey.ChildTraitResponses;
import com.eureka.mindbloom.trait.domain.survey.TraitAnswer;
import com.eureka.mindbloom.trait.domain.survey.TraitQuestion;
import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;
import com.eureka.mindbloom.trait.repository.ChildTraitResponseRepository;
import com.eureka.mindbloom.trait.repository.TraitAnswerRepository;
import com.eureka.mindbloom.trait.repository.TraitQuestionRepository;
import com.eureka.mindbloom.trait.service.ChildHistoryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildHistoryRecordServiceImpl implements ChildHistoryRecordService {

    private final ChildTraitResponseRepository childTraitResponseRepository;
    private final TraitQuestionRepository traitQuestionRepository;
    private final TraitAnswerRepository traitAnswerRepository;

    @Override
    public void saveChildResponse(Child child, List<CreateTraitRequest> answers) {

        // Answers 검증
        List<Integer> questionIds = answers.stream()
                .map(CreateTraitRequest::getQuestionId)
                .collect(Collectors.toList());

        validateQuestion(questionIds);

        List<Integer> answerIds = answers.stream()
                .map(CreateTraitRequest::getAnswerId)
                .collect(Collectors.toList());

        validateAnswer(answerIds);

        // QnA HashMap 생성
        Map<Integer, TraitQuestion> questionMap = traitQuestionRepository.findAllById(questionIds)
                .stream()
                .collect(Collectors.toMap(TraitQuestion::getId, question -> question));

        Map<Integer, TraitAnswer> answerMap = traitAnswerRepository.findAllById(answerIds)
                .stream()
                .collect(Collectors.toMap(TraitAnswer::getId, answer -> answer));

        // ChildTraitResponse HashMap 생성
        List<ChildTraitResponses> existingResponse = childTraitResponseRepository
                .findByChildIdAndQuestionIds(child.getId(),questionIds);

        Map<Integer, ChildTraitResponses> childTraitResponsesMap = existingResponse.stream()
                .collect(Collectors.toMap(response -> response.getQuestion().getId(), response -> response));

        // ChildTraitResponse 저장 및 업데이트
        List<ChildTraitResponses> responses = answers.stream()
                .map(traitData -> {

                    TraitQuestion question = questionMap.get(traitData.getQuestionId());
                    TraitAnswer answer = answerMap.get(traitData.getAnswerId());

                   ChildTraitResponses exResponses = childTraitResponsesMap.get(traitData.getQuestionId());

                    if (exResponses != null) {
                        exResponses.updateAnswer(answer);
                        return exResponses;
                    } else {
                        return ChildTraitResponses.builder()
                                .child(child)
                                .question(question)
                                .answer(answer)
                                .build();
                    }
                })
                .collect(Collectors.toList());

        childTraitResponseRepository.saveAll(responses);
    }

    private void validateQuestion(List<Integer> questionIds) {
        List<TraitQuestion> questions = traitQuestionRepository.findAllById(questionIds);

        if (questions.size() != questionIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일부 질문 Id 찾을 수 없습니다.");
        }
    }

    private void validateAnswer(List<Integer> answerIds) {
        List<TraitAnswer> answers = traitAnswerRepository.findAllById(answerIds);

        if (answers.size() != answerIds.size()) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일부 답변 Id 찾을 수 없습니다.");
        }
    }
}
