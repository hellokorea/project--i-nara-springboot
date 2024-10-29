package com.eureka.mindbloom.Fixtures;

import java.time.LocalDate;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.domain.Member;

public class ChildFixture {
	public static final String CHILD_NAME = "JOE";
	public static final String CHILD_GENDER = "M";
	public static final LocalDate CHILD_BIRTH_DATE = LocalDate.of(2018, 1, 1);

	public static Child getChild(Member parent) {
		return createChild(CHILD_NAME, CHILD_GENDER, CHILD_BIRTH_DATE, parent);
	}

	public static Child createChild(String name, String gender, LocalDate birthDate, Member parent) {
		Child child = Child.builder()
			.name(name)
			.gender(gender)
			.birthDate(birthDate)
			.build();
		child.updateParent(parent);
		return child;
	}
}
