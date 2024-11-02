package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.common.exception.BaseException;
import com.eureka.mindbloom.commoncode.service.CommonCodeConvertService;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.exception.ChildNotFoundException;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.trait.domain.ChildTrait;
import com.eureka.mindbloom.trait.domain.history.TraitRecordHistory;
import com.eureka.mindbloom.trait.domain.history.TraitScoreRecord;
import com.eureka.mindbloom.trait.dto.response.*;
import com.eureka.mindbloom.trait.repository.ChildTraitRepository;
import com.eureka.mindbloom.trait.repository.TraitRecordHistoryRepository;
import com.eureka.mindbloom.trait.repository.TraitScoreRecordRepository;
import com.eureka.mindbloom.trait.service.ChildRecordHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ChildRecordHistoryServiceImpl implements ChildRecordHistoryService {

    private final ChildRepository childRepository;
    private final TraitRecordHistoryRepository traitRecordHistoryRepository;
    private final TraitScoreRecordRepository traitScoreRecordRepository;
    private final ChildTraitRepository childTraitRepository;

    private final CommonCodeConvertService commonCodeConvertService;

    @Override
    @Transactional(readOnly = true)
    public TraitHistoryResponse getHistory(Long childId) {

        Optional<Child> child = childRepository.findById(childId);

        if (child.isEmpty()) {
            throw new ChildNotFoundException(childId);
        }

        // traitRecords
        List<TraitScoreRecord> traitScoreRecords = traitScoreRecordRepository.findByChildAndDeletedAtIsNull(childId);

        if (traitScoreRecords.isEmpty()) {
            throw new BaseException("조회 할 성향 데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        Map<String, Integer> traitRecordData = new HashMap<>();

        for (TraitScoreRecord record : traitScoreRecords) {
            String traitName = commonCodeConvertService.codeToCommonCodeName(record.getTraitCode());
            Integer point = record.getTraitScore();
            traitRecordData.put(traitName, point);
        }

        // actionHistory
        List<FeedbackHistory> feedbackHistories = traitRecordHistoryRepository.getChildHistoryDeletedAtIsNull(childId);

        List<ActionHistory> actionHistories = feedbackHistories.stream()
                .filter(data -> !data.getActionCode().equals("0300_03"))
                .map(data -> ActionHistory.builder()
                        .historyActionId(data.getId())
                        .bookName(data.getBookName())
                        .actionCodeName(commonCodeConvertService.codeToCommonCodeName(data.getActionCode()))
                        .traitCodeName(commonCodeConvertService.codeToCommonCodeName(data.getTraitCode()))
                        .point(data.getPoint())
                        .createdAt( data.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());

        // changeTraitHistory
        List<ChildTrait> childTraits = childTraitRepository.findChildTraitByDeletedAtIsNull(childId);

        // 변경 이력 없을 경우
        if (childTraits.size() < 2) {

            String childTraitValue = childTraits.get(0).getTraitValue().isEmpty() ? null : childTraits.get(0).getTraitValue();

            if (childTraitValue == null) {
                throw new BaseException("자녀 MBTI 성향 값이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
            }

            return TraitHistoryResponse.builder()
                    .childTraitId(childTraits.get(0).getId())
                    .currentTraitValue(childTraits.get(0).getTraitValue())
                    .currentTraitValueCreatedAt(childTraits.get(0).getCreatedAt())
                    .traitRecords(traitRecordData)
                    .actionHistory(actionHistories)
                    .changeTraitHistory(Collections.emptyList())
                    .build();
        }

        // 최신 항목 이후의 모든 변경 이력 추출
        List<TraitHistory> traitHistories = childTraits.subList(1, childTraits.size()).stream()
                .map(data -> TraitHistory.builder()
                        .childTraitId(data.getId())
                        .beforeTraitValue(data.getTraitValue())
                        .createdAt(data.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());

        String currentTraitValue = childTraits.get(0).getTraitValue();

        return TraitHistoryResponse.builder()
                .childTraitId(childTraits.get(0).getId())
                .currentTraitValue(currentTraitValue)
                .currentTraitValueCreatedAt(childTraits.get(0).getCreatedAt())
                .traitRecords(traitRecordData)
                .actionHistory(actionHistories)
                .changeTraitHistory(traitHistories)
                .build();
    }

    @Override
    public ActionFeedbackResponse createChildTraitHistory(Child child, String actionCode, String traitCode, Integer point, String bookName) {

        TraitRecordHistory traitRecordHistory = TraitRecordHistory.builder()
                .child(child)
                .actionCode(actionCode)
                .traitCode(traitCode)
                .bookName(bookName)
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

        String actionCode = "0300_02";
        String traitCode = "0101_03";
        Integer point = 5;
        String book = "어린왕자";

        return createChildTraitHistory(child.get(), actionCode, traitCode, point, book);
    }
}
