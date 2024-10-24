package com.eureka.mindbloom.recommend.eums;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RecommendLikeType {
	LIKE, LIKE_CANCEL;

	@JsonCreator
	public static RecommendLikeType parsing(String inputValue) {
		return Stream.of(RecommendLikeType.values())
			.filter(type -> type.toString().equals(inputValue.toUpperCase()))
			.findFirst()
			.orElse(null);
	}
}
