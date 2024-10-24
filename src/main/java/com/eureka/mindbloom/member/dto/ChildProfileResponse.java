package com.eureka.mindbloom.member.dto;

import com.eureka.mindbloom.member.domain.Child;

public record ChildProfileResponse(
        Long childId,
        String childName,
        String gender
) {
    public static ChildProfileResponse from(Child child) {
        return new ChildProfileResponse(child.getId(), child.getName(), child.getGender());
    }
}