package edu.hsai.lexicalanalyzer.config;

import java.util.ArrayList;
import java.util.Map;

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
        lineDelimiter = String.join("|", (ArrayList<String>) predefinedValues.get("regex_delimiters"));

        var identifierRules = (Map<String, Object>) predefinedValues.get("identifier_rules");
        identifierMaxLength = (long) identifierRules.get("max_length");
        identifierRegex = (String) identifierRules.get("regex");
        identifierMark = (String) identifierRules.get("identifier_mark");
        invalidMark = (String) identifierRules.get("invalid_mark");

        commentChars = (ArrayList<String>) predefinedValues.get("comment_chars");
        aliases = (Map<String, String>) predefinedValues.get("aliases");
    }
}
