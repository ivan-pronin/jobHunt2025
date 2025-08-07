package com.ivan.pronin.job.hunt.senior.box.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
public class FilesWriter {

    void writeToFile(List<String> contents, String fileName){
        try {
            var path = Paths.get(fileName);
            Files.createDirectories(path.getParent());
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            Files.write(path, contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
