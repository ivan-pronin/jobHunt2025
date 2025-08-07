package com.ivan.pronin.job.hunt.senior.box.log.aggregate;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
public class GroupedByDayAccessLogAggregator implements AccessLogAggregator {

    @Override
    public AggregatedLog getAggregatedLog(List<LogEntry> entries) {
        var aggregatedLogs = entries.stream()
            .collect(Collectors.groupingBy(
                entry -> entry.ts().truncatedTo(ChronoUnit.DAYS).toString(),
                Collectors.groupingBy(
                    LogEntry::user,
                    Collectors.mapping(
                        entry -> new AggregatedEntry(entry.file(), entry.operation()),
                        Collectors.toList()
                    ))
            ));

        return new AggregatedLog(aggregatedLogs);
    }

}
