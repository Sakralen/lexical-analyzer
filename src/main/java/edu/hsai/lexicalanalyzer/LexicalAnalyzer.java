package edu.hsai.lexicalanalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import edu.hsai.lexicalanalyzer.inputhandler.FileInputHandler;
import edu.hsai.lexicalanalyzer.predefinedtokens.*;

public class LexicalAnalyzer {
    public static void main(String[] args) {
        String inputFile = "input.txt";
        String inputJson = "predefined_tokens.json";

        if (args.length > 0) {
            inputFile = args[0];
            if (args.length > 1) {
                inputJson = args[1];
            }
        }

        String[] lines = FileInputHandler.readLines(inputFile);
        PredefinedTokens predefined = new PredefinedTokens(inputJson);
        Map<String, String> identifiers = new HashMap<>();

        // Splitting tokens by whitespaces and operators
        Map<Integer, String[]> tokensByLine = new HashMap<>();
        for (int i = 0; i < lines.length; i++) {
            tokensByLine.put(i + 1, Stream.of(lines[i])
                    .flatMap(line -> Stream.of(line.split("\\s+")))
                    .flatMap(tokens -> Stream.of(tokens.split(predefined.getDelimiter())))
                    .toArray(String[]::new));
        }

        // Replacing tokens with marks and adding used tokens to an array
        Map<Integer, String[]> tokensMarked = new HashMap<>();
        ArrayList<String> usedTokens = new ArrayList<>();
        tokensByLine.forEach((k, v) -> tokensMarked.put(k, Stream.of(v).map(token -> {
            if (!usedTokens.contains(token)) {
                usedTokens.add(token);
            }

            if (predefined.getOperators().containsKey(token)) {
                return predefined.getOperators().get(token);
            }
            if (predefined.getKeywords().containsKey(token)) {
                return predefined.getKeywords().get(token);
            }
            if (predefined.getConstants().containsKey(token)) {
                return predefined.getConstants().get(token);
            }

            // TODO: check identifier
            if (!identifiers.containsKey(token)) {
                identifiers.put(token, "I" + (identifiers.size() + 1));
            }
            return identifiers.get(token);
        }).toArray(String[]::new)));

        Arrays.stream(lines).sequential().forEach(System.out::println);
        System.out.println();

        tokensByLine.forEach((k, v) -> {
            System.out.print(k + ": ");
            System.out.println(Arrays.toString(v));
        });
        System.out.println();

        tokensMarked.forEach((k, v) -> {
            System.out.print(k + ": ");
            System.out.println(Arrays.toString(v));
        });
        System.out.println();

        System.out.println("used: " + usedTokens);
    }
}
