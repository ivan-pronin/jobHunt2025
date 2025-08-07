package com.ivan.pronin.job.hunt.senior.box.files;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
public class FilesReader {

    public List<String> readWholeFile(String filePath) {
        try {
            var path = getPath(filePath);
            var allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
            allLines.forEach(System.out::println);
            return allLines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> readLines(String filePath) {
        try (Stream<String> lines = Files.lines(getPath(filePath))) {
            return lines.toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getPath(String fileName) {
        try {
            return Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
