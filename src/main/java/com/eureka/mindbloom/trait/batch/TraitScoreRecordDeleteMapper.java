package com.eureka.mindbloom.trait.batch;

import com.eureka.mindbloom.trait.dto.response.TraitScoreDailyRecordDelete;
import com.eureka.mindbloom.trait.dto.response.TraitScoreRecordDelete;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TraitScoreRecordDeleteMapper implements RowMapper<TraitScoreRecordDelete> {
    private final Map<Long, TraitScoreRecordDelete> recordMap = new HashMap<>();

    @Override
    public TraitScoreRecordDelete mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long tsrId = rs.getLong("id");

        TraitScoreRecordDelete record = recordMap.computeIfAbsent(tsrId, id -> {
            try {
                return new TraitScoreRecordDelete(
                        id,
                        rs.getLong("tsr_child_id"),
                        rs.getString("tsr_trait_code"),
                        new ArrayList<>()
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        if (rs.getLong("tsdr_id") != 0) {
            TraitScoreDailyRecordDelete dailyRecord = new TraitScoreDailyRecordDelete(
                    rs.getLong("tsdr_id"),
                    rs.getString("tsdr_trait_code")
            );
            record.traitScoreDailyRecord().add(dailyRecord);
        }

        return record;
    }
}