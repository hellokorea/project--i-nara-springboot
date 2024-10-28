package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.domain.history.TraitRecordHistory;
import com.eureka.mindbloom.trait.dto.response.ActionFeedbackResponse;
import com.eureka.mindbloom.trait.dto.response.TraitHistoryResponse;
import com.eureka.mindbloom.trait.repository.TraitRecordHistoryRepository;
import com.eureka.mindbloom.trait.service.ChildRecordHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ChildRecordHistoryServiceImpl implements ChildRecordHistoryService {

    private final TraitRecordHistoryRepository traitRecordHistoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TraitHistoryResponse> getHistory(Long childId) {
        return List.of();
    }

    @Override
    public ActionFeedbackResponse createChildTraitHistory(Child child, String actionCode, String traitCode, Integer point) {

        TraitRecordHistory traitRecordHistory = TraitRecordHistory.builder()
                .child(child)
                .actionCode(actionCode)
                .traitCode(traitCode)
                .point(point)
                .build();

        traitRecordHistoryRepository.save(traitRecordHistory);

        return ActionFeedbackResponse.builder()
                .actionCode(traitRecordHistory.getActionCode())
                .traitCode(traitRecordHistory.getTraitCode())
                .point(traitRecordHistory.getPoint())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public void getTotalActionHistoryPoint(List<ActionFeedbackResponse> actionFeedbackResponses) {
        // request 로 받은 애들을 map 에다가 traitCode, point 누적 합 시켜서 trait score table 에 보내서 합산 시켜야함
        // 이 때 백분율 계산법 적용해서 처리 반드시 적용 .. 던져 -> traitScoreRecord 에서 업데이트 처리
    }
}
