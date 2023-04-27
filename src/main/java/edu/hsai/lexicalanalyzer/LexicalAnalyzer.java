package edu.hsai.lexicalanalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import edu.hsai.lexicalanalyzer.filehandler.FileHandler;
import edu.hsai.lexicalanalyzer.config.*;
import edu.hsai.lexicalanalyzer.outputtable.OutputTable;

public class LexicalAnalyzer {
    private final Config cfg;
    private Map<String, String> identifiers = new HashMap<>();
    private Map<String, String> invalidIdentifiers = new HashMap<>();
    private final String[] lines;
    private Map<Integer, String[]> tokenizedLines = new HashMap<>();
    private Map<Integer, String[]> markedLines = new HashMap<>();

    private Map<Integer, String> commentedLines = new HashMap<>();
    private OutputTable results = new OutputTable();

    public LexicalAnalyzer(String pathToFile, String pathToJson) {
        lines = FileHandler.readLines(pathToFile);
        cfg = new Config(pathToJson);

        tokenizeLines();
        analyze();
    }

    private void tokenizeLines() {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isEmpty()) {
                continue;
            }

            // TODO: check comment escape for errors
            tokenizedLines.put(i + 1, Stream.of(lines[i])
                    .flatMap(line -> Stream.of(line.split("\\s+")))
                    .flatMap(tokens -> Stream.of(tokens.split(cfg.lineDelimiter)))
                    .takeWhile(this::isNotCommentStart)
                    .toArray(String[]::new));

        }
    }

    private boolean isNotCommentStart(String token) {
        for (String commentStr : cfg.commentChars) {
            if (token.equals(commentStr)) {
                return false;
            }
        }
        return true;
    }

    private void analyze() {
        tokenizedLines.values().forEach(line -> Stream.of(line).forEach(token -> {
            if (results.contains(token)) {
                return;
            }

            if (cfg.operators.containsKey(token)) {
                results.put(token, cfg.aliases.get("operators"), cfg.operators.get(token));
                return;
            }
            if (cfg.keywords.containsKey(token)) {
                results.put(token, cfg.aliases.get("keywords"), cfg.keywords.get(token));
                return;
            }
            if (cfg.constants.containsKey(token)) {
                results.put(token, cfg.aliases.get("constants"), cfg.constants.get(token));
                return;
            }
            if (identifiers.containsKey(token)) {
                results.put(token, cfg.aliases.get("identifiers"), identifiers.get(token));
                return;
            }
            if (invalidIdentifiers.containsKey(token)) {
                results.put(token, cfg.aliases.get("invalid"), invalidIdentifiers.get(token));
                return;
            }

            if (isIdentifierValid(token)) {
                identifiers.put(token, cfg.identifierMark + (identifiers.size() + 1));
                results.put(token, cfg.aliases.get("identifiers"), identifiers.get(token));
            } else {
                invalidIdentifiers.put(token, cfg.invalidMark + (invalidIdentifiers.size() + 1));
                results.put(token, cfg.aliases.get("invalid"), invalidIdentifiers.get(token));
            }
        }));
    }

    private boolean isIdentifierValid(String identifier) {
        return (identifier.length() <= cfg.identifierMaxLength)
                && identifier.matches(cfg.identifierRegex);
    }

    public OutputTable getResults() {
        return results;
    }
}
