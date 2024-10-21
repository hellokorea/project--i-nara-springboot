package com.eureka.mindbloom.Fixtures;

import com.eureka.mindbloom.member.domain.Member;

public class ParentFixture {
	public static final String PARENT_NAME = "WILLIAM";
	public static final String PARENT_EMAIL = "xxx@naver.com";
	public static final String PARENT_PASSWORD = "password";
	public static final String PARENT_PHONE = "010-1234-5678";
	public static final String USER_ROLE_CODE = "02_0400";

	public static Member getParent() {
		Member parent = createParent(PARENT_NAME, PARENT_EMAIL, PARENT_PASSWORD, PARENT_PHONE);
		return parent;
	}

	private static Member createParent(String name, String email, String password, String phone) {
		Member parent = Member.builder()
			.name(name)
			.email(email)
			.password(password)
			.phone(phone)
			.role(USER_ROLE_CODE)
			.build();
		return parent;
	}
}
