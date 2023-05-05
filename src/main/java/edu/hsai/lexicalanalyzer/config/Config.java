package edu.hsai.lexicalanalyzer.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {
    public final Map<String, String> operators;
    public final Map<String, String> keywords;
    public final Map<String, String> constants;

    public final String lineDelimiter;
    public final String identifierRegex;
    public final long identifierMaxLength;
    public final String identifierMark;
    public final String invalidMark;
    public final ArrayList<String> commentChars;
    public final Map<String, String> aliases;

    public Config(String path) {
        Map<String, Object> predefinedValues = JsonHandler.readJson(path);

        operators = (Map<String, String>) predefinedValues.get("operators");
        keywords = (Map<String, String>) predefinedValues.get("keywords");
        constants = (Map<String, String>) predefinedValues.get("constants");
        lineDelimiter = genLineDelimiter(predefinedValues);

        var identifierRules = (Map<String, Object>) predefinedValues.get("identifier_rules");
        identifierMaxLength = (long) identifierRules.get("max_length");
        identifierRegex = (String) identifierRules.get("regex");
        identifierMark = (String) identifierRules.get("identifier_mark");
        invalidMark = (String) identifierRules.get("invalid_mark");

        commentChars = (ArrayList<String>) predefinedValues.get("comment_chars");
        aliases = (Map<String, String>) predefinedValues.get("aliases");
    }

    public String genLineDelimiter(Map<String, Object> predefinedValues) {
        var operatorsKeys = ((Map<String, String>) predefinedValues.get("operators"))
                .keySet().toArray(String[]::new);
        var commentStrings = ((ArrayList<String>) predefinedValues.get("comment_chars"))
                .toArray(String[]::new);
        var delimiters = Stream.concat(Arrays.stream(operatorsKeys), Arrays.stream(commentStrings))
                .map(this::modifyIntoDelimiter).toArray(String[]::new);

        return String.join("|", delimiters);
    }

    // I didn't test this method properly, so no wonder if it will break smth
    private String modifyIntoDelimiter(String str) {
        Set<String> needsEscape = Stream
                .of("[", "]", "(", ")", "{", "}", "*", "+", "?", "|", "^", "$", ".", "\\")
                .collect(Collectors.toSet());

        String tmp = (!needsEscape.contains(str)) ? str : ("\\" + str);

        return "(?=" + tmp + ")" + "|" + "(?<=" + tmp + ")";
    }
}
