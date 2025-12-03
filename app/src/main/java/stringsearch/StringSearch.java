package stringsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class StringSearch {

    public static void printUsage() {
        System.out.println("Usage: search PATTERN FILE");
    }
    
    public static void main(String[] args) {
        // TODO: add help (-h|--help) flag

        if (args.length != 2) {
            System.out.println("Incorrect number of arguments.");
            StringSearch.printUsage();
            return;
        }

        String pattern = args[0];
        String fileName = args[1];
        File file = new File(fileName);

        ArrayList<String> matches = new ArrayList<>();
        ArrayList<Integer> matchIndices = new ArrayList<>();

        // Read/search File
        try (Scanner reader = new Scanner(file)) {
            int lineNumber = 0;
            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();
                if (currentLine.indexOf(pattern) > -1) {
                    matches.add(currentLine);
                    matchIndices.add(lineNumber);
                }
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            System.out.println(String.format("Could not find file: %s",
                fileName));
            e.printStackTrace();
        }
        
        // Print results

        if(matches.size() == 0) {
            System.out.println(String.format(
                "Found no matches for \"%s\" in file %s", pattern, fileName));
        } else {
            System.out.println(String.format(
                "Found the following matching lines in file %s", fileName));
            
            for (int i = 0; i < matches.size(); i++) {
                System.out.println(matchIndices.get(i) + ": " + matches.get(i));
            }
        }
    }
}
