package com.ivan.pronin.job.hunt.senior.box.log.aggregate;

import java.time.LocalDateTime;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
public record LogEntry(
    LocalDateTime ts,
    String user,
    String file,
    String operation
) {

}
