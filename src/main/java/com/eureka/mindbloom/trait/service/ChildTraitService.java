package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.trait.domain.ChildTrait;
import com.eureka.mindbloom.trait.dto.response.TraitValueResultResponse;

public interface ChildTraitService {
    ChildTrait partiallySaveChildTrait(Child child);

    void finishSaveChildTrait(ChildTrait childTrait, String traitValue);

    TraitValueResultResponse getTraitValueResult(Long childId);

    void softDeleteChildTraits(Member member, Long childId);

    ChildTrait getChildTraitValue(Long childId);
}
