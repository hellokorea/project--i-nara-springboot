package com.eureka.mindbloom.trait.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TraitValue {
	ISTP, ISFP, INFP, INTP, ESTP, ESFP, ENFP, ENTP, ISTJ, ISFJ, INFJ, INTJ, ESTJ, ESFJ, ENFJ, ENTJ;
	public static List<String> getTraitList() {
		return Arrays.stream(TraitValue.values()).map(Enum::name).collect(Collectors.toList());
	}
}
