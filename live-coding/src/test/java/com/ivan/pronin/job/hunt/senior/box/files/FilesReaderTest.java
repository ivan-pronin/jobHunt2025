package com.ivan.pronin.job.hunt.senior.box.files;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
class FilesReaderTest {

    private final FilesReader reader = new FilesReader();

    @Test
    void readWholeFile() {
        final List<String> strings = reader.readWholeFile("input/text.txt");
        Assertions.assertEquals(4, strings.size());
    }

    @Test
    void readLinesFromFile() {
        final List<String> strings = reader.readLines("input/text.txt");
        Assertions.assertEquals(4, strings.size());
    }

}