package com.eureka.mindbloom.event.service.impl;

import com.eureka.mindbloom.common.exception.BaseException;
import com.eureka.mindbloom.event.service.EventService;
import com.eureka.mindbloom.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final StringRedisTemplate eventRedisTemplate;

    private final AtomicBoolean eventActive = new AtomicBoolean(false);

    private static final String EVENT_KEY = "event";
    private static final String SCRIPT =
            "local user_id = ARGV[1] " +
                    "local local_date_time = ARGV[2] " +
                    "local max_participants = tonumber(redis.call('get', 'event:maxParticipants')) " +
                    "local event_key = KEYS[1] " +

                    "if redis.call('hlen', event_key) >= max_participants then " +
                    "    return 'event_full' " +
                    "end " +

                    "if redis.call('hexists', event_key, user_id) == 1 then " +
                    "    return 'already_issued' " +
                    "end " +

                    "redis.call('hset', event_key, user_id, local_date_time) " +
                    "return 'issued'";


    @Override
    public String submitEventEntry(Member member) {
        if (!isEventActive()) {
            return "이벤트가 아직 시작되지 않았습니다.";
        }

        String memberId = member.getId().toString();

        Instant instant = Instant.now();
        String localDateTime = instant.getEpochSecond() + "." + instant.getNano();

        RedisScript<String> redisScript = RedisScript.of(SCRIPT, String.class);

        String result = eventRedisTemplate.execute(redisScript,
                List.of(EVENT_KEY),
                memberId, localDateTime);

        return switch (Objects.requireNonNull(result)) {
            case "already_issued" -> "이미 티켓이 발급된 사용자입니다.";
            case "issued" -> "티켓이 발급되었습니다.";
            case "event_full" -> "마감된 이벤트입니다.";
            default -> throw new BaseException("알 수 없는 에러가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @Override
    public void startEvent(Integer eventId) {
        eventActive.set(true);
        log.info("선착순 이벤트 시작 : {}", eventId);
    }

    @Override
    public void endEvent(Integer eventId) {
        eventActive.set(false);
        log.info("선착순 이벤트 종료 : {}", eventId);
    }

    private boolean isEventActive() {
        return eventActive.get();
    }
}