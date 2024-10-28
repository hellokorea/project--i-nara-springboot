package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.exception.ChildNotFoundException;
import com.eureka.mindbloom.trait.domain.ChildTrait;
import com.eureka.mindbloom.trait.dto.response.TraitValueResultResponse;
import com.eureka.mindbloom.trait.repository.ChildTraitRepository;
import com.eureka.mindbloom.trait.service.ChildHistoryRecordService;
import com.eureka.mindbloom.trait.service.ChildTraitService;
import com.eureka.mindbloom.trait.service.TraitScoreRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class ChildTraitServiceImpl implements ChildTraitService {

    private final ChildTraitRepository childTraitRepository;
    private final TraitScoreRecordService traitScoreRecordService;
    private final ChildHistoryRecordService childHistoryRecordService;

    @Override
    public ChildTrait partiallySaveChildTrait(Child child) {

        ChildTrait childTrait = ChildTrait.builder()
                .child(child)
                .traitGroupCode("0101")
                .build();

        childTraitRepository.save(childTrait);

        return childTrait;
    }

    @Override
    public void finishSaveChildTrait(ChildTrait childTrait, String traitValue) {
        childTrait.update(traitValue);
        childTraitRepository.save(childTrait);
    }

    @Override
    public TraitValueResultResponse getTraitValueResult(Long childId) {

        ChildTrait childTrait = childTraitRepository.findByChildId(childId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자녀는 MBTI 검사를 실시하지 않았습니다"));

        Map<String, Integer> childTraitScores = traitScoreRecordService.getChildTraitScores(childTrait.getChild());

        return TraitValueResultResponse.builder()
                .traitValue(childTrait.getTraitValue())
                .valueData(childTraitScores)
                .build();
    }

    @Override
    public void softDeleteChildTraits(Member member, Long childId) {
        if (isNotParent(member, childId)) {
            throw new ChildNotFoundException(childId);
        }
        childTraitRepository.softDeleteChildTrait(childId);
        childHistoryRecordService.softDeleteChildResponse(childId);
    }

    private boolean isNotParent(Member member, Long childId) {
        return member.getChildren().stream().noneMatch(child -> child.getId().equals(childId));
    }
}
