package com.eureka.mindbloom.category.domain;

import com.eureka.mindbloom.member.domain.Child;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildPreferred {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String categoryCode;

	@ManyToOne
	@JoinColumn(name = "child_id")
	private Child child;

	public ChildPreferred(String categoryCode, Child child) {
		this.categoryCode = categoryCode;
		this.child = child;
	}
}
