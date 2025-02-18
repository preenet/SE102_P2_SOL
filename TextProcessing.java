import java.io.*;
import java.util.*;

public class TextProcessing {
    public static void main(String[] args) {
        String filename = "input_large.txt";
        long startTime = System.nanoTime();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int charCount = 0;
            int palindromeCount = 0;
            int tokenCount = 0;
            int emoticonCount = 0;
            int newLineCount = 0;
            List<Integer> tokenSizes = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                newLineCount++;
                charCount += line.length();

                // Tokenize the line using non-word characters as delimiters
                String[] tokens = line.split("\\W+");
                tokenCount += tokens.length;
                
                for (String token : tokens) {
                    if (!token.isEmpty()) {
                        tokenSizes.add(token.length());
                        if (isPalindrome(token)) {
                            palindromeCount++;
                        }
                    }
                }

                // Detect emoticons (basic smiley and sad faces)
                emoticonCount += countEmoticons(line);
            }

            int longestToken = tokenSizes.isEmpty() ? 0 : Collections.max(tokenSizes);
            double avgTokenSize = 0;
            if (!tokenSizes.isEmpty()) {
                int sum = 0;
                for (int size : tokenSizes) {
                    sum += size;
                }
                avgTokenSize = (double) sum / tokenSizes.size();
            }
            
            long endTime = System.nanoTime();

            System.out.println("Program start:\n");
            System.out.println("Total # Character count: " + charCount);
            System.out.println("Total # Palindrome found: " + palindromeCount);
            System.out.println("Total Number of tokens: " + tokenCount);
            System.out.println("Total Number of emoticon: " + emoticonCount);
            System.out.println("Total # of new line: " + newLineCount);
            System.out.println("Longest token size: " + longestToken);
            System.out.printf("Average token size: %.2f\n", avgTokenSize);
            System.out.printf("Total time to execute this program: %.2f secs\n", (endTime - startTime) / 1e9);
            System.out.println("\nProgram terminated properly!");

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static boolean isPalindrome(String word) {
        word = word.toLowerCase();
        int left = 0, right = word.length() - 1;
        while (left < right) {
            if (word.charAt(left) != word.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return word.length() > 1; // Only count words longer than 1 character
    }

    private static int countEmoticons(String line) {
        String[] emoticons = {":)", ":(", ":D", ";)", ":P", ":o", ":O", "XD", ":-)", ":-D", ":-P"};
        int count = 0;
        for (String emoticon : emoticons) {
            int index = line.indexOf(emoticon);
            while (index != -1) {
                count++;
                index = line.indexOf(emoticon, index + 1);
            }
        }
        return count;
    }
}
