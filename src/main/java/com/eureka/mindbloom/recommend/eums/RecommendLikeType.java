package com.eureka.mindbloom.recommend.eums;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RecommendLikeType {
	LIKE("추천좋아요"), LIKE_CANCEL("추천좋아요취소");

	public String actionName;

	RecommendLikeType(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	@JsonCreator
	public static RecommendLikeType parsing(String inputValue) {
		return Stream.of(RecommendLikeType.values())
			.filter(type -> type.toString().equals(inputValue.toUpperCase()))
			.findFirst()
			.orElse(null);
	}
}
