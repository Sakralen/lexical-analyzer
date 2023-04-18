package edu.hsai.lexicalanalyzer.inputhandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class FileInputHandler {
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
