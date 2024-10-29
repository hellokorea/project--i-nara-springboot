package com.eureka.mindbloom.trait.service.impl;

import com.eureka.mindbloom.trait.domain.history.TraitScoreDailyRecord;
import com.eureka.mindbloom.trait.domain.history.TraitScoreRecord;
import com.eureka.mindbloom.trait.repository.TraitScoreDailyRecordRepository;
import com.eureka.mindbloom.trait.service.TraitScoreDailyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class TraitScoreDailyRecordServiceImpl implements TraitScoreDailyRecordService {

    private final TraitScoreDailyRecordRepository traitScoreDailyRecordRepository;

    @Override
    public void createDailyHistoryRecord(Map<String, List<TraitScoreRecord>> groupedRecords,
                                         Map<String, Integer> dailyTraitTotalPoints) {

        List<TraitScoreDailyRecord> dailyRecords = new ArrayList<>();

        groupedRecords.forEach((groupKey, records) -> {

            records.forEach(record -> {
                String traitCode = record.getTraitCode();
                Integer historyScore = dailyTraitTotalPoints.getOrDefault(traitCode, 0);

                if (historyScore != 0) {
                    dailyRecords.add(
                            TraitScoreDailyRecord.builder()
                                    .traitScoreRecord(record)
                                    .traitCode(traitCode)
                                    .traitScore(historyScore)
                                    .build());
                }
            });
        });

        traitScoreDailyRecordRepository.saveAll(dailyRecords);
    }
}
