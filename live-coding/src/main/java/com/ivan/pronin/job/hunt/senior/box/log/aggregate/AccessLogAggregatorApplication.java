package com.ivan.pronin.job.hunt.senior.box.log.aggregate;

import java.util.Scanner;

public class AccessLogAggregatorApplication {

    private final AccessLogPrinter accessLogPrinter;

    private final AccessLogRecorder accessLogRecorder;

    private final AccessLogAggregator accessLogAggregator;

    public AccessLogAggregatorApplication(AccessLogPrinter accessLogPrinter,
                                          AccessLogRecorder accessLogRecorder,
                                          AccessLogAggregator accessLogAggregator) {
        this.accessLogPrinter = accessLogPrinter;
        this.accessLogRecorder = accessLogRecorder;
        this.accessLogAggregator = accessLogAggregator;
    }

    public AggregatedLog run() {
        var sc = new Scanner(System.in);
        while (true) {
            var nextLine = sc.nextLine();
            if (nextLine.isEmpty() || nextLine.equals("exit")) break;
            if (nextLine.equals("AGGREGATE")) {
                var aggregatedLog = accessLogAggregator.getAggregatedLog(accessLogRecorder.getAll());
                accessLogPrinter.printResult(aggregatedLog);
                return aggregatedLog;
            }
            var tokens = nextLine.split(" ");
            accessLogRecorder.record(tokens);
        }
        var aggregatedLog = accessLogAggregator.getAggregatedLog(accessLogRecorder.getAll());
        accessLogPrinter.printResult(aggregatedLog);
        sc.close();
        return aggregatedLog;
    }

}
