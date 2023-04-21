package edu.hsai.lexicalanalyzer.filehandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {
    public static String[] readLines(String path) {
        String[] lines = null;
        try {
            lines = Files.readAllLines(Path.of(path)).toArray(String[]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}
