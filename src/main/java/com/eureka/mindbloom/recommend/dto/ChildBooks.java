package com.eureka.mindbloom.recommend.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildBooks {
	public Long childId;
	public List<String> recommendBooksIsbn;
	public Map<String, Integer> traitBooksIsbn = new HashMap<>();

	public ChildBooks(Long childId, List<String> recommendBooksIsbn) {
		this.childId = childId;
		this.recommendBooksIsbn = recommendBooksIsbn;
	}
}
