package com.ivan.pronin.job.hunt.senior.box.log.aggregate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
public class DefaultAccessLogRecorder implements AccessLogRecorder {

    private final List<LogEntry> logs = new ArrayList<>();

    @Override
    public void record(String[] tokens) {
        if (tokens.length < 4) {
            throw new IllegalArgumentException("Invalid input, skipping: " + Arrays.toString(tokens));
        }
        var ts = LocalDateTime.parse(tokens[0]);
        var user = tokens[1];
        var file = tokens[2];
        var operation = tokens[3];
        logs.add(new LogEntry(ts, user, file, operation));
    }

    @Override
    public List<LogEntry> getAll() {
        return new ArrayList<>(logs);
    }

}
