package com.eureka.mindbloom.common.config;

import com.eureka.mindbloom.common.domain.TimeZoneEnum;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(TimeZoneEnum.ASIA_SEOUL.getZoneId()));
    }
}
