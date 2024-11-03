package com.eureka.mindbloom.member.dto;

import com.eureka.mindbloom.member.domain.Child;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public record ChildProfileResponse(
        Long childId,
        String childName,
        String gender,
        LocalDate birthDate,
        int age, // 나이
        List<String> categories
) {
    public static ChildProfileResponse from(Child child) {

        List<String> categories = child.getPreferredContents().stream()
                .map(childPreferred -> childPreferred.getCategoryCode()) // category 필드 직접 사용
                .collect(Collectors.toList());

        // 나이 계산
        int age = Period.between(child.getBirthDate(), LocalDate.now()).getYears();

        return new ChildProfileResponse(child.getId(), child.getName(), child.getGender(), child.getBirthDate(), age, categories);
    }
}