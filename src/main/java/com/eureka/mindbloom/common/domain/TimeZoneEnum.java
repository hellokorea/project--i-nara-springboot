package com.eureka.mindbloom.common.domain;

import lombok.Getter;

@Getter
public enum TimeZoneEnum {
    UTC("UTC"),
    ASIA_SEOUL("Asia/Seoul"),
    AMERICA_NEW_YORK("America/New_York");

    private final String zoneId;

    TimeZoneEnum(String zoneId) {
        this.zoneId = zoneId;
    }
}
