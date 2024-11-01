package com.eureka.mindbloom.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatGPTService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 캐시용 Map 선언 (Key: plot, Value: MBTI 예측 결과)
    private final Map<String, String> responseCache = new ConcurrentHashMap<>();

    // 유효한 MBTI 유형 집합
    private static final Set<String> VALID_MBTI_TYPES = new HashSet<>(Arrays.asList(
            "INTJ", "INTP", "INFJ", "INFP", "ISTJ", "ISTP", "ISFJ", "ISFP",
            "ENTJ", "ENTP", "ENFJ", "ENFP", "ESTJ", "ESTP", "ESFJ", "ESFP"
    ));

    private static final int BATCH_SIZE = 10; // 한 번에 처리할 줄거리 개수
    private static final long REQUEST_INTERVAL_MS = 5000; // 배치 처리 간격 (5초)

    /**
     * 한 번에 여러 개의 plot을 배치로 예측
     */
    public void predictMBTIBatch(List<String> plots) {
        int totalPlots = plots.size();
        int startIndex = 0;

        while (startIndex < totalPlots) {
            int endIndex = Math.min(startIndex + BATCH_SIZE, totalPlots);
            List<String> batchPlots = plots.subList(startIndex, endIndex);

            for (String plot : batchPlots) {
                String result = predictMBTI(plot);
                System.out.println("Plot: " + plot + ", Predicted MBTI: " + result);
            }

            startIndex += BATCH_SIZE;

            // 다음 배치 처리를 위해 대기 시간 설정
            if (startIndex < totalPlots) {
                try {
                    System.out.println("Batch completed. Waiting for next batch...");
                    Thread.sleep(REQUEST_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Batch processing interrupted.");
                    break;
                }
            }
        }
    }

    public String predictMBTI(String plot) {
        // 캐시에서 해당 plot에 대한 결과 확인
        if (responseCache.containsKey(plot)) {
            return responseCache.get(plot);  // 캐시된 결과 반환
        }

        // 최대 재시도 횟수까지 MBTI 예측 시도
        for (int attempt = 1; attempt <= 3; attempt++) {
            String result = requestMBTIPrediction(plot);
            String mbtiType = extractMBTIType(result);

            if (VALID_MBTI_TYPES.contains(mbtiType)) {
                responseCache.put(plot, mbtiType); // 캐시에 저장
                return mbtiType;
            }

            // 재시도 시 메시지 출력
            System.out.println("유효하지 않은 MBTI 응답: " + result + " - 재시도 중 (" + attempt + "/3)");
        }

        return "할당량이 초과되었습니다. 나중에 다시 시도해 주세요.";
    }

    private String requestMBTIPrediction(String plot) {
        String prompt = "이 줄거리의 예상 MBTI 유형은? \"" + plot + "\"";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);
        headers.set("Content-Type", "application/json");

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", "gpt-3.5-turbo"); // 비용 절감 모델 사용
        requestBody.put("max_tokens", 10); // 토큰 절약을 위해 낮은 값 설정

        ObjectNode message = objectMapper.createObjectNode();
        message.put("role", "user");
        message.put("content", prompt);

        ArrayNode messages = objectMapper.createArrayNode();
        messages.add(message);

        requestBody.set("messages", messages);

        HttpEntity<String> request;
        try {
            String body = objectMapper.writeValueAsString(requestBody);
            request = new HttpEntity<>(body, headers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "JSON parsing error";
        }

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.openai.com/v1/chat/completions",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText().trim();

        } catch (HttpClientErrorException.TooManyRequests e) {
            System.out.println("할당량 초과 발생. 재시도 중...");
            return "할당량이 초과되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown error";
        }
    }

    private String extractMBTIType(String response) {
        // 응답에서 MBTI 유형으로 추정되는 4글자 추출
        String cleanedResponse = response.replaceAll("[^A-Z]", "").trim();
        return cleanedResponse.length() >= 4 ? cleanedResponse.substring(0, 4) : "";
    }
}
