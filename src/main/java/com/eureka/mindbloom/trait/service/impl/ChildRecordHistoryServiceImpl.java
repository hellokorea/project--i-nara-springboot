package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.exception.ChildNotFoundException;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.trait.domain.history.TraitRecordHistory;
import com.eureka.mindbloom.trait.dto.response.ActionFeedbackResponse;
import com.eureka.mindbloom.trait.dto.response.TraitHistoryResponse;
import com.eureka.mindbloom.trait.repository.TraitRecordHistoryRepository;
import com.eureka.mindbloom.trait.service.ChildRecordHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ChildRecordHistoryServiceImpl implements ChildRecordHistoryService {

    private final ChildRepository childRepository;

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
                .traitCode(traitRecordHistory.getTraitCode())
                .point(traitRecordHistory.getPoint())
                .build();
    }

    @Override
    public ActionFeedbackResponse testPost(Long childId) {
        Optional<Child> child = childRepository.findById(childId);

        if (child.isEmpty()) {
            throw new ChildNotFoundException(childId);
        }

        String actionCode = "0300";
        String traitCode = "0101_03";
        Integer point = 5;

        return createChildTraitHistory(child.get(), actionCode, traitCode, point);
    }
}
