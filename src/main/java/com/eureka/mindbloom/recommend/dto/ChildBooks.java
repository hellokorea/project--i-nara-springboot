package com.eureka.mindbloom.recommend.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildBooks {
	public Long childId;
	public List<String> recentBooksIsbn;
	public List<String> traitBooksIsbn;

	public ChildBooks(Long childId, List<String> recentBooksIsbn) {
		this.childId = childId;
		this.recentBooksIsbn = recentBooksIsbn;
	}
}
