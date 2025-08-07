package com.ivan.pronin.job.hunt.senior.box.log.aggregate;

import java.util.List;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
public interface AccessLogAggregator {

    AggregatedLog getAggregatedLog(List<LogEntry> entries);

}
