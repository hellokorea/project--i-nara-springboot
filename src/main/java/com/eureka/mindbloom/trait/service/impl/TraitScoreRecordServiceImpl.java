package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.common.exception.BaseException;
import com.eureka.mindbloom.commoncode.domain.CommonCode;
import com.eureka.mindbloom.commoncode.service.CommonCodeConvertService;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.exception.ChildNotFoundException;
import com.eureka.mindbloom.trait.domain.ChildTrait;
import com.eureka.mindbloom.trait.domain.history.TraitScoreRecord;
import com.eureka.mindbloom.trait.domain.survey.TraitAnswer;
import com.eureka.mindbloom.trait.dto.response.ActionFeedbackResponse;
import com.eureka.mindbloom.trait.dto.response.TraitPointsResponse;
import com.eureka.mindbloom.trait.dto.response.UpdateTraitBatchResponse;
import com.eureka.mindbloom.trait.repository.ChildTraitRepository;
import com.eureka.mindbloom.trait.repository.TraitRecordHistoryRepository;
import com.eureka.mindbloom.trait.repository.TraitScoreRecordRepository;
import com.eureka.mindbloom.trait.service.TraitScoreRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TraitScoreRecordServiceImpl implements TraitScoreRecordService {

    private final TraitScoreRecordRepository traitScoreRecordRepository;
    private final TraitRecordHistoryRepository traitRecordHistoryRepository;
    private final ChildTraitRepository childTraitRepository;

    private final CommonCodeConvertService commonCodeConvertService;

    @Override
    public List<TraitPointsResponse> saveTraitScoreRecord(ChildTrait childTrait,
                                                          Map<Integer, TraitAnswer> responseAnswersMap) {

        List<CommonCode> allTraitCodes = commonCodeConvertService.codeGroupToCommonCodes("0101");

        Map<String, Integer> traitScores = allTraitCodes.stream()
                .map(CommonCode::getCode)
                .collect(Collectors.toMap(traitCode -> traitCode, traitCode -> 0));

        responseAnswersMap.forEach((id, answer) -> {
            String traitCode = answer.getTraitCode();
            Integer point = answer.getPoint();

            traitScores.put(traitCode, traitScores.getOrDefault(traitCode, 0) + point);
        });

        List<TraitScoreRecord> traitScoreRecords = new ArrayList<>();
        traitScores.forEach((traitCode, traitScore) -> {

            TraitScoreRecord traitScoreRecord = TraitScoreRecord.builder()
                    .child(childTrait.getChild())
                    .traitCode(traitCode)
                    .traitScore(traitScore)
                    .build();

            traitScoreRecords.add(traitScoreRecord);
        });

        traitScoreRecordRepository.saveAll(traitScoreRecords);

        return traitScores.entrySet().stream()
                .map(entry -> TraitPointsResponse.builder()
                        .traitCode(entry.getKey())
                        .traitScore(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public String fetchTraitValue(List<TraitPointsResponse> traitScores) {

        StringBuilder sb = new StringBuilder();
        Map<String, Integer> mbtiMap = new HashMap<>();

        traitScores.forEach((response) -> {
            String traitCode = response.getTraitCode();
            Integer sumScore = response.getTraitScore();
            String piece = fetchTraitNamePiece(traitCode);

            mbtiMap.put(piece, mbtiMap.getOrDefault(piece, 0) + sumScore);
        });

        sb.append(mbtiMap.get("E") >= mbtiMap.get("I") ? "E" : "I");
        sb.append(mbtiMap.get("N") >= mbtiMap.get("S") ? "N" : "S");
        sb.append(mbtiMap.get("T") >= mbtiMap.get("F") ? "T" : "F");
        sb.append(mbtiMap.get("J") >= mbtiMap.get("P") ? "J" : "P");

        return sb.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> getChildTraitResult(Child child) {

        Map<String, Integer> result = new HashMap<>();

        List<TraitScoreRecord> traitScoreRecords = traitScoreRecordRepository
                .findByChildAndDeletedAtIsNull(child.getId());

        if (traitScoreRecords.isEmpty()) {
            throw new BaseException("해당 자녀의 검사 기록 데이터가 삭제 되었거나 없습니다.", HttpStatus.NOT_FOUND);
        }

        traitScoreRecords.forEach(data -> {
            String traitNamePiece = fetchTraitNamePiece(data.getTraitCode());
            result.put(traitNamePiece, result.getOrDefault(traitNamePiece, 0) + data.getTraitScore());
        });

        return result;
    }

    private String fetchTraitNamePiece(String code) {
        return commonCodeConvertService.codeToCommonCodeName(code);
    }

    // ---------------------------------- Batch Logic
    @Override
    public Map<String, Integer> calculateDailyTraitPoints(Child child) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1);

            return traitRecordHistoryRepository.getChildActionHistoryDeletedAtIsNull(child.getId(), startOfDay, endOfDay).stream()
                    .collect(Collectors.toMap(
                            ActionFeedbackResponse::getTraitCode,
                            ActionFeedbackResponse::getPoint,
                            Integer::sum
                    ));
    }

    @Override
    public UpdateTraitBatchResponse updateTraitPointsBatch(Child child, Map<String, Integer> dailyTraitTotalPoints) {

        if (dailyTraitTotalPoints.isEmpty()) {
            log.info("자녀의 MBTI 일일 기록 데이터가 없어 스킵되었습니다. 자녀 ID: {}", child.getId());
            return null;
        }

        List<TraitScoreRecord> traitScoreRecords = traitScoreRecordRepository.findByChildAndDeletedAtIsNull(child.getId());

        if (traitScoreRecords.isEmpty()) {
            log.info("자녀의 MBTI 점수 기록 데이터가 없어 스킵되었습니다. 자녀 ID: {}", child.getId());
            return null;
        }


        // 그룹별 traitCode 리스트 정의
        List<CommonCode> allTraitCodes = commonCodeConvertService.codeGroupToCommonCodes("0101");

        Map<String, List<String>> traitGroups = allTraitCodes.stream()
                .collect(Collectors.groupingBy(
                        code -> {
                            switch (code.getCode()) {
                                case "0101_01", "0101_02" -> { return "IE"; }
                                case "0101_03", "0101_04" -> { return "SN"; }
                                case "0101_05", "0101_06" -> { return "TF"; }
                                case "0101_07", "0101_08" -> { return "PJ"; }
                                default -> { return "Unknown"; }
                            }
                        },
                        Collectors.mapping(CommonCode::getCode, Collectors.toList())
                ));

        Map<String, Integer> groupScores = new HashMap<>();
        Map<String, List<TraitScoreRecord>> groupedRecords = new HashMap<>();

        // 그룹별 점수 합산 및 레코드 분류
        for (TraitScoreRecord record : traitScoreRecords) {
            String traitCode = record.getTraitCode();
            Integer currentScore = record.getTraitScore();

            Integer historyScore = dailyTraitTotalPoints.getOrDefault(traitCode, 0);
            Integer newTotalScore = currentScore + historyScore;

            traitGroups.forEach((groupKey, codes) -> {
                if (codes.contains(traitCode)) {
                    groupScores.put(groupKey, groupScores.getOrDefault(groupKey, 0) + newTotalScore);
                    groupedRecords.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(record);
                }
            });
        }

        List<TraitPointsResponse> newChildTraitValue = new ArrayList<>();

        // 그룹별로 백분율 조정 비율 계산 및 업데이트
        groupedRecords.forEach((groupKey, records) -> {
            int totalScore = groupScores.getOrDefault(groupKey, 0);
            double adjustmentRatio = 100.0 / totalScore;

            records.forEach(record -> {
                String traitCode = record.getTraitCode();
                Integer currentScore = record.getTraitScore();
                Integer historyScore = dailyTraitTotalPoints.getOrDefault(traitCode, 0);
                int newTotalScore = currentScore + historyScore;

                Integer adjustedScore = (int) Math.floor(newTotalScore * adjustmentRatio);

                record.updateScore(adjustedScore);

                TraitPointsResponse updateChildTrait = TraitPointsResponse.builder()
                        .traitCode(record.getTraitCode())
                        .traitScore(adjustedScore)
                        .build();

                newChildTraitValue.add(updateChildTrait);
            });
        });

        traitScoreRecordRepository.saveAll(traitScoreRecords);

        return UpdateTraitBatchResponse.builder()
                .child(child)
                .groupedCodes(groupedRecords)
                .dailyTraitTotalPoints(dailyTraitTotalPoints)
                .newChildTraitValue(fetchTraitValue(newChildTraitValue))
                .build();
    }

    @Override
    public void createNewChildTraitValue(Child child, String newChildTraitValue) {

        Pageable pageable = PageRequest.of(0, 1);
        List<String> currentChildTrait = childTraitRepository.findChildCurrentTraitByTraitValue(child.getId(), pageable);

        if (currentChildTrait.isEmpty()) {
            throw new ChildNotFoundException(child.getId());
        }

        if (currentChildTrait.get(0).equals(newChildTraitValue)) {
            return;
        }

        ChildTrait childTrait = ChildTrait.builder()
                .child(child)
                .traitValue(newChildTraitValue)
                .traitGroupCode("0101")
                .build();

        childTraitRepository.save(childTrait);
    }
}
