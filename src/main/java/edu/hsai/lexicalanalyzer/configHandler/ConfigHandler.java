package edu.hsai.lexicalanalyzer.configHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class ConfigHandler {
    private final Map predefinedTokens;

    public ConfigHandler(String path) {
        try (Reader reader = Files.newBufferedReader(Path.of(path))) {
            predefinedTokens = new GsonBuilder()
                    .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                    .create().fromJson(reader, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!predefinedTokens.containsKey("operators")
                || !predefinedTokens.containsKey("constants")
                || !predefinedTokens.containsKey("keywords")
                || !predefinedTokens.containsKey("regex_delimiters")
                || !predefinedTokens.containsKey("identifier_rules")
                || !predefinedTokens.containsKey("comment_chars")
                || !predefinedTokens.containsKey("aliases")) {
            throw new RuntimeException();
        }
    }

    public Map<String, String> getOperators() {
        return (Map<String, String>) predefinedTokens.get("operators");
    }

    public Map<String, String> getKeywords() {
        return (Map<String, String>) predefinedTokens.get("keywords");
    }

    public Map<String, String> getConstants() {
        return (Map<String, String>) predefinedTokens.get("constants");
    }

    public String getRegexDelimiter() {
        return String.join("|", (ArrayList<String>) predefinedTokens.get("regex_delimiters"));
    }

    public long getIdentifierMaxLength() {
        var identifierRules = (Map<String, Object>) predefinedTokens.get("identifier_rules");
        return (long) identifierRules.get("max_length");
    }

    public String getIdentifierRegex() {
        var identifierRules = (Map<String, Object>) predefinedTokens.get("identifier_rules");
        return (String) identifierRules.get("regex");
    }

    public String getIdentifierMark() {
        var identifierRules = (Map<String, Object>) predefinedTokens.get("identifier_rules");
        return (String) identifierRules.get("mark");
    }

    public String[] getCommentChars() {
        return (String[]) predefinedTokens.get("comment_chars");
    }

    public Map<String, String> getAliases() {
        return (Map<String, String>) predefinedTokens.get("aliases");
    }
}
