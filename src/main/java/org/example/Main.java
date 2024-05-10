package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static int top = 0;
    public static int phraseSize = 0;

    public static void main(String[] args) {
        if (args.length != 3 ) {
            System.out.println("Open from CommandLine: java -jar jarFileName.jar <file_path> -top= <top_phrases> -phraseSize= <phrase_size>");
            return;
        }

        String filePath = args[0];

        for (int i = 1; i < args.length; i++) {
            try {
                if (args[i].startsWith("-top=")) {
                    top = Integer.parseInt(args[i].substring(5));
                    if (top <= 0) {
                        System.out.println("Invalid top value");
                        return;
                    }
                } else if (args[i].startsWith("-phraseSize=")) {
                    phraseSize = Integer.parseInt(args[i].substring(12));
                    if (phraseSize <= 0) {
                        System.out.println("Invalid phraseSize value");
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer value: " + e.getMessage());
                return;
            }
        }

        String text = readText(filePath);

        int numberOfWords = numberOfWords(text);
        int numberOfPhrases = numberOfPhrases(text);

        System.out.println("\noutput:\n+--------------------------------+-------+");
        System.out.printf("| %-30s | %5d |\n", "Number of words:", numberOfWords);
        System.out.println("+-------------------------------+-------+");
        System.out.printf("| %-30s | %5d |\n", "Number of sentences:", numberOfPhrases);
        System.out.println("+--------------------------------+-------+");

        Map<String, Integer> topPhrases = topPhrases(text, top, phraseSize);
        System.out.println("\n+--------------------------------+-------+");
        System.out.println("| Phrases                        | Count |");
        System.out.println("+--------------------------------+-------+");
        printTopPhrases(topPhrases);
    }

    static String readText(String filePath) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line.replaceAll("[\",:']", "")).append("\n");

            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return text.toString();
    }

    static int numberOfWords(String text) {
        String[] cuvinte = text.split("\\s+");
        return cuvinte.length;
    }

    static int numberOfPhrases(String text) {
        Pattern pattern = Pattern.compile("[.!?]+");
        Matcher matcher = pattern.matcher(text);
        int numberOfPhrases = 0;
        while (matcher.find()) {
            numberOfPhrases++;
        }
        return numberOfPhrases;
    }

    static Map<String, Integer> topPhrases(String text, int top, int phraseSize) {
        Map<String, Integer> phraseCount = new HashMap<>();
        String[] sentences = text.split("[.!?]");
        for (String sentence : sentences) {
            String[] words = sentence.trim().split("\\s+");
            for (int i = 0; i < words.length - phraseSize + 1; i++) {
                String phrase = String.join(" ", Arrays.copyOfRange(words, i, i + phraseSize));
                phraseCount.put(phrase, phraseCount.getOrDefault(phrase, 0) + 1);
            }
        }
        return topEntries(phraseCount, top);
    }

    private static Map<String, Integer> topEntries(Map<String, Integer> map, int top) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        Map<String, Integer> topEntries = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(top, list.size()); i++) {
            topEntries.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return topEntries;
    }

    static void printTopPhrases(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.printf("| %-30s | %5d |\n", entry.getKey(), entry.getValue());
        }
        System.out.println("+--------------------------------+-------+");
    }
}
