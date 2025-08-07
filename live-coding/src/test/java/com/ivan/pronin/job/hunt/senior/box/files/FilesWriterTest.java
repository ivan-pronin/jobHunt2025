package com.ivan.pronin.job.hunt.senior.box.files;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
class FilesWriterTest {

    private final FilesWriter writer = new FilesWriter();

    @Test
    void writeToFile() {
        var contents = List.of("1st row", "2nd rod");
        writer.writeToFile(contents, "output/out.txt");
    }

}