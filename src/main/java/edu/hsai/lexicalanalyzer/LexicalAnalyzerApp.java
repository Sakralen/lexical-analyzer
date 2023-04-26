package edu.hsai.lexicalanalyzer;

public class LexicalAnalyzerApp {
    public static void main(String[] args) {
        String pathToFile = "input.txt";
        String pathToJson = "config.json";

        if (args.length > 0) {
            pathToFile = args[0];
        }
        if (args.length > 1) {
            pathToJson = args[1];
        }
        if (args.length > 2) {
            System.out.println("Invalid args!");
            System.exit(1);
        }

        LexicalAnalyzer analyzer = new LexicalAnalyzer(pathToFile, pathToJson);


        System.out.println(analyzer.getResults());
    }
}
