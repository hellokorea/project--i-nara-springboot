package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.trait.domain.survey.TraitAnswer;
import com.eureka.mindbloom.trait.domain.survey.TraitQuestion;
import com.eureka.mindbloom.trait.dto.request.AdminQnARequest;
import com.eureka.mindbloom.trait.dto.response.AdminQnADeleteResponse;
import com.eureka.mindbloom.trait.dto.response.Answer;
import com.eureka.mindbloom.trait.dto.response.AdminQnAResponse;
import com.eureka.mindbloom.trait.repository.TraitAnswerRepository;
import com.eureka.mindbloom.trait.repository.TraitQuestionRepository;
import com.eureka.mindbloom.trait.service.AdminTraitSurveyService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTraitSurveyServiceImpl implements AdminTraitSurveyService {

    private final TraitQuestionRepository traitQuestionRepository;
    private final TraitAnswerRepository traitAnswerRepository;

    @Override
    public List<AdminQnAResponse> getQnA() {
        List<TraitQuestion> traitQuestions = traitQuestionRepository.findAll();
        List<TraitAnswer> traitAnswers = traitAnswerRepository.findAll();

        return traitQuestions.stream()
                .map(q -> {

                    List<Answer> relatedAnswers = traitAnswers.stream()
                            .filter(answer -> answer.getQuestion().getId().equals(q.getId()))
                            .map(a -> Answer.builder()
                                        .answerId(a.getId())
                                        .content(a.getContent())
                                        .traitCode(a.getTraitCode())
                                        .point(a.getPoint())
                                        .build())
                            .collect(Collectors.toList());

                    return AdminQnAResponse.builder()
                            .questionId(q.getId())
                            .traitCodeGroup(q.getTraitCodeGroup())
                            .content(q.getContent())
                            .disabled(q.isDisabled())
                            .choices(relatedAnswers)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public AdminQnAResponse createQnA(AdminQnARequest adminQnARequest) {

        validateQuestion(adminQnARequest);
        validateAnswer(adminQnARequest.getChoices());

        // Question 생성
        TraitQuestion createToQuestion = TraitQuestion.builder()
                .traitCodeGroup("0101")
                .content(adminQnARequest.getQuestionContent())
                .disabled(adminQnARequest.isDisabled())
                .build();

        traitQuestionRepository.save(createToQuestion);

        // Answer 생성
        List<Answer> createToAnswers = adminQnARequest.getChoices().stream()
                .map(answer -> {

                    TraitAnswer answerData = TraitAnswer.builder()
                            .question(createToQuestion)
                            .content(answer.getContent())
                            .traitCode(answer.getTraitCode())
                            .point(answer.getPoint())
                            .build();

                    traitAnswerRepository.save(answerData);

                    return Answer.builder()
                            .answerId(answerData.getId())
                            .content(answerData.getContent())
                            .traitCode(answerData.getTraitCode())
                            .point(answerData.getPoint())
                            .build();
                })
                .collect(Collectors.toList());

        return AdminQnAResponse.builder()
                .questionId(createToQuestion.getId())
                .traitCodeGroup(createToQuestion.getTraitCodeGroup())
                .content(createToQuestion.getContent())
                .disabled(createToQuestion.isDisabled())
                .choices(createToAnswers)
                .build();
    }

    @Override
    public AdminQnAResponse updateQnA(Integer questionId, AdminQnARequest adminQnARequest) {

        validateQuestion(adminQnARequest);
        validateAnswer(adminQnARequest.getChoices());

        Optional<TraitQuestion> optionalQuestion = traitQuestionRepository.findById(questionId);
        List<TraitAnswer> existingAnswers = traitAnswerRepository.findTraitAnswerByQuestionId(questionId);

        validateQnARepository(optionalQuestion, existingAnswers);

        // Question 업데이트
        TraitQuestion updateToQuestion = optionalQuestion.get();
        updateToQuestion.update(adminQnARequest.getQuestionContent(), adminQnARequest.isDisabled());
        traitQuestionRepository.save(updateToQuestion);

        // Answer 업데이트
        List<Answer> updateToAnswers = existingAnswers.stream()
                .limit(adminQnARequest.getChoices().size())
                .map(answer -> {

                    int index = existingAnswers.indexOf(answer);
                    Answer answerDto = adminQnARequest.getChoices().get(index);
                    answer.update(answerDto.getContent(), answerDto.getTraitCode(), answerDto.getPoint());

                    traitAnswerRepository.save(answer);

                    return Answer.builder()
                            .answerId(answer.getId())
                            .content(answer.getContent())
                            .traitCode(answer.getTraitCode())
                            .point(answer.getPoint())
                            .build();
                })
                .collect(Collectors.toList());

        return AdminQnAResponse.builder()
                .questionId(updateToQuestion.getId())
                .traitCodeGroup(updateToQuestion.getTraitCodeGroup())
                .content(updateToQuestion.getContent())
                .disabled(updateToQuestion.isDisabled())
                .choices(updateToAnswers)
                .build();
    }

    @Override
    public AdminQnADeleteResponse deleteQnA(Integer questionId) {

        Optional<TraitQuestion> optionalQuestion = traitQuestionRepository.findById(questionId);
        List<TraitAnswer> existingAnswers = traitAnswerRepository.findTraitAnswerByQuestionId(questionId);

        validateQnARepository(optionalQuestion, existingAnswers);

        TraitQuestion deleteToQuestion = optionalQuestion.get();

        // Answer 삭제
        List<Integer> answerIds = existingAnswers.stream()
                .map(TraitAnswer::getId)
                .collect(Collectors.toList());

        traitAnswerRepository.deleteAllInBatch(existingAnswers);

        // Question 삭제
        traitQuestionRepository.deleteById(deleteToQuestion.getId());

        return AdminQnADeleteResponse.builder()
                .questionId(deleteToQuestion.getId())
                .answerIds(answerIds)
                .build();
    }

    private void validateQuestion(AdminQnARequest adminQnARequest) {
        if (adminQnARequest == null || adminQnARequest.getQuestionContent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "입력한 질문 데이터가 올바르지 않습니다.");
        }
    }

    private void validateAnswer(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 데이터가 비어있습니다.");
        }

        for (Answer answer : answers) {

            if (answer.getContent() == null || answer.getTraitCode() == null || answer.getPoint() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 Dto 데이터가 비어있습니다.");
            }
        }
    }

    private void validateQnARepository(Optional<TraitQuestion> question, List<TraitAnswer> answers) {
        if (question.isEmpty() || answers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 질문 및 답변 데이터를 찾을 수 없습니다.");
        }
    }
}
