package edu.hsai.lexicalanalyzer.predefinedtokens;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class PredefinedTokens {
    private final Map<String, String> operators;
    private final Map<String, String> keywords;
    private final Map<String, String> constants;
    private final String delimiter;

    public PredefinedTokens(String path) {
        Map predefinedTokens;

        try (Reader reader = Files.newBufferedReader(Path.of(path))) {
            predefinedTokens = new Gson().fromJson(reader, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!predefinedTokens.containsKey("operators")
                || !predefinedTokens.containsKey("constants")
                || !predefinedTokens.containsKey("keywords")
                || !predefinedTokens.containsKey("regex_splitters")) {
            throw new RuntimeException();
        }

        operators = (Map<String, String>)predefinedTokens.get("operators");
        constants = (Map<String, String>)predefinedTokens.get("constants");
        keywords = (Map<String, String>)predefinedTokens.get("keywords");
        delimiter = String.join("|", (ArrayList<String>)predefinedTokens.get("regex_delimiters"));
    }

    public Map<String, String> getOperators() {
        return operators;
    }

    public Map<String, String> getKeywords() {
        return keywords;
    }

    public Map<String, String> getConstants() {
        return constants;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
