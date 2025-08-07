package com.ivan.pronin.job.hunt.senior.box;

import java.io.ByteArrayInputStream;
import java.util.Map;

import com.google.gson.Gson;
import com.ivan.pronin.job.hunt.senior.box.log.aggregate.AccessLogAggregator;
import com.ivan.pronin.job.hunt.senior.box.log.aggregate.AccessLogAggregatorApplication;
import com.ivan.pronin.job.hunt.senior.box.log.aggregate.AccessLogPrinter;
import com.ivan.pronin.job.hunt.senior.box.log.aggregate.AccessLogRecorder;
import com.ivan.pronin.job.hunt.senior.box.log.aggregate.DefaultAccessLogRecorder;
import com.ivan.pronin.job.hunt.senior.box.log.aggregate.GroupedByDayAccessLogAggregator;
import com.ivan.pronin.job.hunt.senior.box.log.aggregate.JsonAccessLogPrinter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
class AccessLogAggregatorApplicationTest {

    private final AccessLogPrinter printer = new JsonAccessLogPrinter(new Gson());

    private final AccessLogRecorder logRecorder = new DefaultAccessLogRecorder();

    private final AccessLogAggregator aggregator = new GroupedByDayAccessLogAggregator();

    private final AccessLogAggregatorApplication application = new AccessLogAggregatorApplication(printer, logRecorder, aggregator);

    @Test
    void run() {
        String input = String.join("\n",
            "2025-08-06T15:34:10 user1 /docs/file1.txt READ",
            "2025-08-06T15:34:12 user1 /docs/file2.txt WRITE",
            "2025-08-06T15:34:13 user2 /docs/x.txt READ",
            "2025-08-06T15:34:15 user1 /docs/file1.txt READ",
            "AGGREGATE",
            "exit") + "\n";

        var originalIn = System.in;
        try {

            var testIn = new ByteArrayInputStream(input.getBytes());
            System.setIn(testIn);

            var actualResult = application.run().accessLogs();
            var expectedResult = Map.of(
                "user1", Map.of("READ", 2, "WRITE", 1),
                "user2", Map.of("READ", 1)
            );
            assertEquals(expectedResult, actualResult);
        } finally {
            System.setIn(originalIn);
        }
    }

}