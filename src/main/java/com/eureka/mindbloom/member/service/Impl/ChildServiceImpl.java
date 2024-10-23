package com.eureka.mindbloom.member.service.Impl;

import com.eureka.mindbloom.category.domain.ChildPreferred;
import com.eureka.mindbloom.common.exception.NotFoundException;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.dto.ChildProfileResponse;
import com.eureka.mindbloom.member.dto.ChildRegisterRequest;
import com.eureka.mindbloom.member.dto.ChildRegisterResponse;
import com.eureka.mindbloom.member.dto.UpdateChildRequest;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.member.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;

    @Override
    public ChildRegisterResponse registerChild(Member parents, ChildRegisterRequest request) {
        Child child = childRepository.save(
                Child.builder()
                        .name(request.name())
                        .birthDate(request.birthDate())
                        .gender(request.gender())
                        .parent(parents)
                        .build()
        );

        List<ChildPreferred> preferred = request.categories().stream()
                .map(category -> new ChildPreferred(category, child))
                .toList();

        child.addPreferredContent(preferred);

        return ChildRegisterResponse.from(child);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildProfileResponse> getChildProfile(Member parents) {
        List<Child> children = childRepository.findByParentId(parents.getId());

        return children.stream().map(ChildProfileResponse::from)
                .toList();
    }

    @Override
    public void updateChildProfile(Member member, Long childId, UpdateChildRequest request) {
        Child child = childRepository.findByParentIdAndId(member.getId(), childId)
                .orElseThrow(() -> NotFoundException.childNotFound(childId));

        List<ChildPreferred> preferred = request.categories().stream()
                .map(category -> new ChildPreferred(category, child))
                .collect(Collectors.toList());

        child.updateChild(request.name(), preferred);
    }
}