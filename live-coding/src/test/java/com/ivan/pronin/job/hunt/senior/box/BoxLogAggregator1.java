package com.ivan.pronin.job.hunt.senior.box;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 05.08.2025
 */
public class BoxLogAggregator1 {

    public void run() {
        Map<String, List<String>> actions = new LinkedHashMap<>();
        var sc = new Scanner(System.in);
        System.out.println("Waiting for logs input. Type 'exit' (without apostrophes) to stop the program.");
        while (true) {
            var line = sc.nextLine();
            if (line.isEmpty() || line.equals("exit")) break;
            var tokens = line.trim().split(" ");
            if (tokens.length < 3) {
                System.err.println("Invalid input line, skipping: " + line);
                continue;
            }
            var user = tokens[1];
            var action = tokens[2];
            actions.computeIfAbsent(user, key -> new ArrayList<>()).add(action);
        }
        if (actions.isEmpty()) {
            System.out.println("No logs were recorded");
            return;
        }
        actions.forEach((key, value) -> System.out.println(key + ": " + String.join(" ", value)));
        sc.close();
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
            System.setOut(printOut);

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
