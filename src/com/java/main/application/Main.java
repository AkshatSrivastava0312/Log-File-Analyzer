package com.java.main.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
    	
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("************** Welcome to Log Analyzer **************\n");

            System.out.println("Enter your file location in the format C:\\Users\\John Doe\\file.log");
            String fileName = sc.nextLine();
            Path logFilePath = Paths.get(fileName);

            boolean isRunning = true;

            while (isRunning) {
                List<String> fileLines = Files.lines(logFilePath).map(String::toUpperCase).collect(Collectors.toList());

                System.out.println("Press '1' to get default log highlights");
                System.out.println("Press '2' to get customized log highlights");
                System.out.println("Press '3' to quit");
                System.out.print("Enter your choice: ");
                
                String choice = sc.nextLine();
                System.out.println();

                switch (choice) {
                    case "1":
                        analyzeDefaultLogs(fileLines);
                        break;

                    case "2":
                        analyzeCustomLogs(sc, fileLines, fileName);
                        break;

                    case "3":
                        isRunning = false;
                        break;

                    default:
                        System.err.println("Invalid Choice. Please try again.");
                }
            }
            System.out.println("Exiting Application.....\nApplication Closed");
        } catch (IOException e) {
            System.err.println("Unable to locate the file: " + e.getMessage());
            System.out.println("Exiting Application.....\nApplication Closed");
        }
    }

    private static void analyzeDefaultLogs(List<String> fileLines) {
        System.out.println("Analyzing your file....\n");
        System.out.println("******************** Default LOG Highlights ********************\n");
        printLogHighlights(fileLines, "ERROR");
        printLogHighlights(fileLines, "WARNING");
        printLogHighlights(fileLines, "SUCCESS");
    }

    private static void printLogHighlights(List<String> fileLines, String keyword) {
        List<String> messages = fileLines.stream().filter(line -> line.contains(keyword)).collect(Collectors.toList());
        long count = messages.size();
        List<Integer> lineNumbers = IntStream.range(0, fileLines.size())
                                            .filter(i -> fileLines.get(i).contains(keyword))
                                            .map(i -> i + 1)
                                            .boxed()
                                            .collect(Collectors.toList());

        System.out.println("---------------------- " + keyword + " MESSAGES ----------------------");
        System.out.println("Total " + keyword + " Messages: " + count + "\n");

        for (int i = 0; i < count; i++) {
            System.out.println("Message Number: " + (i + 1));
            System.out.println("Line Number: " + lineNumbers.get(i));
            System.out.println("Message Description: " + messages.get(i));
            System.out.println();
        }
    }

    private static void analyzeCustomLogs(Scanner sc, List<String> fileLines, String fileName) throws IOException {
        System.out.println("Enter number of keywords you want to search for:");
        int numKeywords = sc.nextInt();
        sc.nextLine(); // Consume newline

        System.out.println("Press '1' if you want keywords to be case-sensitive");
        System.out.println("Press '2' if you do not want keywords to be case-sensitive");
        System.out.print("Enter your choice: ");
        String caseChoice = sc.nextLine();
        
        List<String> customKeywords = new ArrayList<>();
        for (int i = 0; i < numKeywords; i++) {
            System.out.print("Enter Keyword " + (i + 1) + ": ");
            customKeywords.add(sc.nextLine());
        }

        if ("1".equals(caseChoice)) {
            CustomKeywords.logsForCustomKeywordsCaseSensitive(customKeywords, fileName);
        } else if ("2".equals(caseChoice)) {
            CustomKeywords.logsForCustomKeywordsCaseInSensitive(customKeywords, fileName);
        } else {
            System.err.println("Invalid choice");
        }
    }
}
