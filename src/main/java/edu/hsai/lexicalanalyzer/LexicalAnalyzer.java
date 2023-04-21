package edu.hsai.lexicalanalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import edu.hsai.lexicalanalyzer.filehandler.FileHandler;
import edu.hsai.lexicalanalyzer.configHandler.*;

public class LexicalAnalyzer {
    private final Map<String, String> operators;
    private final Map<String, String> keywords;
    private final Map<String, String> constants;
    private Map<String, String> identifiers = new HashMap<>();

    private final String lineDelimiter;
    private final String identifierRegex;
    private final int identifierMaxLength;
    private final String identifierMark;

    private final String[] lines;
    private Map<Integer, String[]> tokenizedLines = new HashMap<>();
    private Map<Integer, String[]> markedLines = new HashMap<>();

    private ArrayList<String> usedTokens;

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

        analyze();
    }

    private void analyze() {
        tokenizeLines();
        markLines();
        System.out.println("hi!");
    }

    private void tokenizeLines() {
        // TODO: ignore comments!
        for (int i = 0; i < lines.length; i++) {
            tokenizedLines.put(i + 1, Stream.of(lines[i])
                    .flatMap(line -> Stream.of(line.split("\\s+")))
                    .flatMap(tokens -> Stream.of(tokens.split(lineDelimiter)))
                    .toArray(String[]::new));
        }
    }

    private void markLines() {
        tokenizedLines.forEach((k, v) -> markedLines.put(k, Stream.of(v).map(token -> {
            // TODO: put tokens in used!
            if (operators.containsKey(token)) {
                return operators.get(token);
            }
            if (keywords.containsKey(token)) {
                return keywords.get(token);
            }
            if (constants.containsKey(token)) {
                return constants.get(token);
            }

            if (!identifiers.containsKey(token)) {
                if (isIdentifierValid(token)) {
                    identifiers.put(token, identifierMark + (identifiers.size() + 1));
                } else {
                    // TODO: state this error in results!
                    throw new RuntimeException();
                }
            }
            return identifiers.get(token);
        }).toArray(String[]::new)));
    }

    private boolean isIdentifierValid(String identifier) {
        return (identifier.length() <= identifierMaxLength)
                && identifier.matches(identifierRegex);
    }
}
