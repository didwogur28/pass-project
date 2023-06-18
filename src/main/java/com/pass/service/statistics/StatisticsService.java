package com.pass.service.statistics;

import com.pass.repository.statistics.StatisticsRepository;
import com.pass.util.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public Object makeChartData(LocalDateTime to) {

        final LocalDateTime from = to.minusDays(10);

        final List<AggregatedStatistics> aggregatedStatisticsList = statisticsRepository.findByStatisticsAtBetweenAndGroupBy(from, to);

        // 라벨, 출석 횟수, 취소 횟수
        List<String> labels = new ArrayList<>();
        List<Long> attendedCounts = new ArrayList<>();
        List<Long> concelledCounts = new ArrayList<>();

        for (AggregatedStatistics statistics : aggregatedStatisticsList) {

            labels.add(LocalDateTimeUtils.format(statistics.getStatisticsAt(), LocalDateTimeUtils.MM_DD));
            attendedCounts.add(statistics.getAttendedCount());
            concelledCounts.add(statistics.getCancelledCount());
        }

        return new ChartData(labels, attendedCounts, concelledCounts);
    }
}
