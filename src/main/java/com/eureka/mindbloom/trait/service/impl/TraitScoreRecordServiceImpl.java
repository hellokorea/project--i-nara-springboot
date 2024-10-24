package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.domain.ChildTrait;
import com.eureka.mindbloom.trait.domain.history.TraitScoreRecord;
import com.eureka.mindbloom.trait.domain.survey.TraitAnswer;
import com.eureka.mindbloom.trait.dto.response.TraitPointsResponse;
import com.eureka.mindbloom.trait.repository.TraitScoreRecordRepository;
import com.eureka.mindbloom.trait.service.TraitScoreRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class TraitScoreRecordServiceImpl implements TraitScoreRecordService {

    private final TraitScoreRecordRepository traitScoreRecordRepository;

    @Override
    public List<TraitPointsResponse> saveTraitScoreRecord(ChildTrait childTrait,
                                                          Map<Integer, TraitAnswer> responseAnswersMap) {

        List<String> allTraitCodes = List.of("0101_01", "0101_02", "0101_03", "0101_04",
                                             "0101_05", "0101_06", "0101_07", "0101_08");

        Map<String, Integer> traitScores = allTraitCodes.stream()
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

        traitScores.forEach((answer) -> {
            String traitCode = answer.getTraitCode();
            Integer sumScore = answer.getTraitScore();
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
    public Map<String, Integer> getChildTraitScores(Child child) {

        Map<String, Integer> result = new HashMap<>();

        List<TraitScoreRecord> traitScoreRecords = traitScoreRecordRepository
                .findByChildAndDeletedAtIsNull(child.getId());

        if (traitScoreRecords.isEmpty()) {
            throw new NoSuchElementException("해당 자녀의 검사 기록 데이터가 삭제 되었거나 없습니다.");
        }

        traitScoreRecords.forEach(data -> {
            String traitNamePiece = fetchTraitNamePiece(data.getTraitCode());
            result.put(traitNamePiece, result.getOrDefault(traitNamePiece, 0) + data.getTraitScore());
        });

        return result;
    }

    private String fetchTraitNamePiece(String piece) {
        return switch (piece) {
            case "0101_01" -> "E";
            case "0101_02" -> "I";
            case "0101_03" -> "S";
            case "0101_04" -> "N";
            case "0101_05" -> "T";
            case "0101_06" -> "F";
            case "0101_07" -> "P";
            case "0101_08" -> "J";
            default -> "Unknown";
        };
    }
}
