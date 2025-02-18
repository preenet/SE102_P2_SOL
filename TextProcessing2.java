import java.io.*;
import java.util.*;

class TextFile {
    String filename;
    int charCount;
    int palindromeCount;
    int tokenCount;
    int emoticonCount;
    int newLineCount;
    int longestToken;
    double avgTokenSize;
    double executionTime;

    public TextFile(String filename, int charCount, int palindromeCount, int tokenCount, int emoticonCount,
                    int newLineCount, int longestToken, double avgTokenSize, double executionTime) {
        this.filename = filename;
        this.charCount = charCount;
        this.palindromeCount = palindromeCount;
        this.tokenCount = tokenCount;
        this.emoticonCount = emoticonCount;
        this.newLineCount = newLineCount;
        this.longestToken = longestToken;
        this.avgTokenSize = avgTokenSize;
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "Filename: " + filename + "\n" +
               "Character Count: " + charCount + "\n" +
               "Palindrome Count: " + palindromeCount + "\n" +
               "Token Count: " + tokenCount + "\n" +
               "Emoticon Count: " + emoticonCount + "\n" +
               "New Line Count: " + newLineCount + "\n" +
               "Longest Token Size: " + longestToken + "\n" +
               "Average Token Size: " + avgTokenSize + "\n" +
               "Execution Time: " + executionTime + " secs\n";
    }
}

public class TextProcessing2 {
    public static void main(String[] args) {
        String filename = "input.txt";
        long startTime = System.nanoTime();
        List<TextFile> textFiles = new ArrayList<>();

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

                // Detect emoticons and emojis
                emoticonCount += countEmoticons(line) + countEmojis(line);
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
            double executionTime = (endTime - startTime) / 1e9;

            TextFile textFile = new TextFile(filename, charCount, palindromeCount, tokenCount, emoticonCount,
                                             newLineCount, longestToken, avgTokenSize, executionTime);
            textFiles.add(textFile);

            for (TextFile tf : textFiles) {
                System.out.println(tf);
            }
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
    
    private static int countEmojis(String line) {
        List<String> emojis = EmojiParser.extractEmojis(line);
        return emojis.size();
    }
}
