package com.ivan.pronin.job.hunt.senior.box.log.aggregate;

import java.util.Map;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
public record AccessLog(
    Map<String, Map<String, Integer>> accessLogs
) {

}
