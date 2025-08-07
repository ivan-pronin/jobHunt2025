package com.ivan.pronin.job.hunt.senior.box;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 05.08.2025
 */
public class BoxLogAggregator2 {

    static class LogEntry {

        long timestamp;

        String action;

        LogEntry(long timestamp, String action) {
            this.timestamp = timestamp;
            this.action = action;
        }

    }

    public void run() {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        Map<String, Map<String, List<LogEntry>>> data = new TreeMap<>(); // day -> user -> list

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty() || line.equals("exit")) break;

            String[] tokens = line.trim().split(" ");
            if (tokens.length < 3) continue;

            long timestamp = Long.parseLong(tokens[0]);
            String user = tokens[1];
            String action = tokens[2];

            String day = Instant.ofEpochSecond(timestamp)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
                .toString(); // "YYYY-MM-DD"

            data.computeIfAbsent(day, d -> new TreeMap<>())
                .computeIfAbsent(user, u -> new ArrayList<>())
                .add(new LogEntry(timestamp, action));
        }

        // сортируем каждый список по timestamp
        for (var userMap : data.values()) {
            for (var logs : userMap.values()) {
                logs.sort(Comparator.comparingLong(log -> log.timestamp));
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(data));
    }

    @Test
    public void testNormalInput() throws IOException {
        String input = String.join("\n",
            "1691200001 alice login",
            "1691200002 bob view",
            "1691200003 alice logout",
            "1691200004 bob logout",
            "exit"
        ) + "\n";

        String expectedOutput = String.join("\n",
            "alice: login logout",
            "bob: view logout"
        ) + "\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try (
            ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream testOut = new ByteArrayOutputStream();
            PrintStream printOut = new PrintStream(testOut)
        ) {
            System.setIn(testIn);
//            System.setOut(printOut);

            this.run();

            String actualOutput = testOut.toString();
            assertTrue(actualOutput.contains("alice: login logout"));
            assertTrue(actualOutput.contains("bob: view logout"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

}
