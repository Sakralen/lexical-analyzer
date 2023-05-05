package edu.hsai.lexicalanalyzer.config;

import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class JsonHandler {
    public static Map<String, Object> readJson(String path) {
        Map<String, Object> predefinedValues;
        try (Reader reader = Files.newBufferedReader(Path.of(path))) {
            predefinedValues = new GsonBuilder()
                    .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                    .create().fromJson(reader, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!predefinedValues.containsKey("operators")
                || !predefinedValues.containsKey("constants")
                || !predefinedValues.containsKey("keywords")
                || !predefinedValues.containsKey("identifier_rules")
                || !predefinedValues.containsKey("comment_chars")
                || !predefinedValues.containsKey("aliases")) {
            throw new RuntimeException();
        }

        return predefinedValues;
    }
}