package com.ivan.pronin.job.hunt.senior.box.log.aggregate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
public class JsonAccessLogPrinter implements AccessLogPrinter {

    private final Gson gson;

    public JsonAccessLogPrinter(Gson gson) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void printResult(AggregatedLog aggregatedLog) {
        System.out.println(gson.toJson(aggregatedLog));
    }

}
