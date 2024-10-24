package com.eureka.mindbloom.recommend.dto;

import com.eureka.mindbloom.recommend.eums.RecommendLikeType;

import jakarta.validation.constraints.NotNull;

public record RecommendBookLikeRequest (@NotNull Long bookRecommendId, @NotNull(message = "유효하지 않는 likeType이 입력되었습니다.") RecommendLikeType likeType ) {
}
