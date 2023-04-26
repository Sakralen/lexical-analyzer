package edu.hsai.lexicalanalyzer.outputtable;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class OutputTable {
    private static final String formatter = "%32s";
    private record Pair<K, V>(K key, V value) {
        @Override
        public String toString() {
            return String.format(formatter, key) + " | " + String.format("%4s", value);
        }
    };

    private Map<String, Pair<String, String>> results = new LinkedHashMap<>();

    public boolean put(String token, String type, String mark) {
        if (!results.containsKey(token)) {
            results.put(token, new Pair<>(type, mark));
            return true;
        }
        return false;
    }

    public boolean contains(String token) {
        return results.containsKey(token);
    }

    @Override
    public String toString() {
        if (results.size() != 0) {
            StringBuilder sb = new StringBuilder();
            results.forEach((key, value) -> sb
                    .append(String.format(formatter, key))
                    .append(" | ")
                    .append(String.format(formatter, value))
                    .append("\n"));
            return sb.toString();
        } else {
            return "Таблица результатов пуста!";
        }
    }
}
