package com.ivan.pronin.job.hunt.senior.box.log.aggregate;

import java.util.List;
import java.util.Map;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
public record AggregatedLog(
    Map<String, Map<String, List<AggregatedEntry>>> accessLogs
) {

}
