package com.eureka.mindbloom.Fixtures;

import java.util.List;

import com.eureka.mindbloom.category.domain.ChildPreferred;
import com.eureka.mindbloom.member.domain.Child;

public class ChildPreferredFixture {
	public static final String CATEGORY_CODE_LEARNING = "0201";
	public static final String CATEGORY_CODE_NATURE = "0202";
	public static final String CATEGORY_CODE_DAILY_LIFE = "0203";

	public static List<ChildPreferred> getChildPreferredList(Child child) {
		List<ChildPreferred> childPreferredList = List.of(
			createChildPreferred(CATEGORY_CODE_LEARNING, child),
			createChildPreferred(CATEGORY_CODE_NATURE, child),
			createChildPreferred(CATEGORY_CODE_DAILY_LIFE, child)
		);
		return childPreferredList;
	}

	public static ChildPreferred createChildPreferred(String categoryCode, Child child) {
		ChildPreferred childPreferred = new ChildPreferred(categoryCode, child);
		return childPreferred;
	}
}
