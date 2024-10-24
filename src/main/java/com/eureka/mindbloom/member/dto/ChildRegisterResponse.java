package com.eureka.mindbloom.member.dto;

import com.eureka.mindbloom.member.domain.Child;

import java.time.LocalDate;

public record ChildRegisterResponse(
        Long childId,
        String name,
        LocalDate birthDate
) {
    public static ChildRegisterResponse from(Child child) {
        return new ChildRegisterResponse(child.getId(), child.getName(), child.getBirthDate());
    }
}