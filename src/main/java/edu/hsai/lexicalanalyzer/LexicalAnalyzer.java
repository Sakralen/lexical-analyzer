package edu.hsai.lexicalanalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import edu.hsai.lexicalanalyzer.filehandler.FileHandler;
import edu.hsai.lexicalanalyzer.confighandler.*;
import edu.hsai.lexicalanalyzer.outputtable.OutputTable;

public class LexicalAnalyzer {
    // TODO: migrate final fields to a subclass
    private final Map<String, String> operators;
    private final Map<String, String> keywords;
    private final Map<String, String> constants;
    private Map<String, String> identifiers = new HashMap<>();
    private Map<String, String> invalidIdentifiers = new HashMap<>();

    private final String lineDelimiter;
    private final String identifierRegex;
    private final int identifierMaxLength;
    private final String identifierMark;
    private final ArrayList<String> commentChars;
    private final Map<String, String> aliases;

    private final String[] lines;
    private Map<Integer, String[]> tokenizedLines = new HashMap<>();
    private Map<Integer, String[]> markedLines = new HashMap<>();

    private Map<Integer, String> commentedLines = new HashMap<>();
    private OutputTable results = new OutputTable();

    public LexicalAnalyzer(String pathToFile, String pathToJson) {
        lines = FileHandler.readLines(pathToFile);

        ConfigHandler config = new ConfigHandler(pathToJson);
        operators = config.getOperators();
        keywords = config.getKeywords();
        constants = config.getConstants();
        lineDelimiter = config.getRegexDelimiter();
        identifierRegex = config.getIdentifierRegex();
        identifierMaxLength = (int)config.getIdentifierMaxLength();
        identifierMark = config.getIdentifierMark();
        commentChars = config.getCommentChars();
        aliases = config.getAliases();

        tokenizeLines();
        analyze();
    }

    private void tokenizeLines() {
        for (int i = 0; i < lines.length; i++) {
            boolean isComment = false;
            for (String commentStr: commentChars) {
                if (lines[i].startsWith(commentStr)) {
                    commentedLines.put(i + 1, lines[i]);
                    isComment = true;
                    break;
                }
            }

            if (!isComment) {
                tokenizedLines.put(i + 1, Stream.of(lines[i])
                        .flatMap(line -> Stream.of(line.split("\\s+")))
                        .flatMap(tokens -> Stream.of(tokens.split(lineDelimiter)))
                        .toArray(String[]::new));
            }
        }
    }

    private void analyze() {
        tokenizedLines.values().forEach(line -> Stream.of(line).forEach(token -> {
            if (results.contains(token)) {
                return;
            }

            if (operators.containsKey(token)) {
                results.put(token, aliases.get("operators"), operators.get(token));
                return;
            }
            if (keywords.containsKey(token)) {
                results.put(token, aliases.get("keywords"), keywords.get(token));
                return;
            }
            if (constants.containsKey(token)) {
                results.put(token, aliases.get("constants"), constants.get(token));
                return;
            }
            if (identifiers.containsKey(token)) {
                results.put(token, aliases.get("identifiers"), identifiers.get(token));
                return;
            }
            if (invalidIdentifiers.containsKey(token)) {
                // TODO: put in cfg
                results.put(token, "Invalid identifier", invalidIdentifiers.get(token));
                return;
            }

            if (isIdentifierValid(token)) {
                identifiers.put(token, identifierMark + (identifiers.size() + 1));
                results.put(token, aliases.get("identifiers"), identifiers.get(token));
            } else {
                // TODO: put in cfg
                invalidIdentifiers.put(token, "E" + (invalidIdentifiers.size() + 1));
                results.put(token, "Invalid identifier", invalidIdentifiers.get(token));
            }
        }));
    }

    private boolean isIdentifierValid(String identifier) {
        return (identifier.length() <= identifierMaxLength)
                && identifier.matches(identifierRegex);
    }

    public OutputTable getResults() {
        return results;
    }
}
