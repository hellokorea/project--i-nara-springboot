package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.trait.domain.history.TraitScoreRecord;

import java.util.List;
import java.util.Map;

public interface TraitScoreDailyRecordService {
    void createDailyHistoryRecord(Map<String, List<TraitScoreRecord>> groupedRecords,
                                  Map<String, Integer> dailyTraitTotalPoints);
}
