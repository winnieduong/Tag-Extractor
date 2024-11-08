import java.io.*;
import java.util.*;

public class TagExtractor {
    private File inputFile;
    private File stopWordsFile;
    private Set<String> stopWords;
    private Map<String, Integer> wordFrequency;

    public TagExtractor() {
        stopWords = new HashSet<>();
        wordFrequency = new HashMap<>();
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public void setStopWordsFile(File stopWordsFile) {
        this.stopWordsFile = stopWordsFile;
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getStopWordsFile() {
        return stopWordsFile;
    }

    public String extractTags() {
        loadStopWords();
        wordFrequency.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().replaceAll("[^a-z]", " ").split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty() && !stopWords.contains(word)) {
                        wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            return "Error reading input file: " + e.getMessage();
        }

        return generateTagOutput();
    }

    private void loadStopWords() {
        stopWords.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(stopWordsFile))) {
            String word;
            while ((word = reader.readLine()) != null) {
                stopWords.add(word.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading stop words file: " + e.getMessage());
        }
    }

    private String generateTagOutput() {
        StringBuilder result = new StringBuilder();
        wordFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n"));
        return result.toString();
    }

    public boolean saveTagsToFile(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print(generateTagOutput());
            return true;
        } catch (IOException e) {
            System.err.println("Error saving tags to file: " + e.getMessage());
            return false;
        }
    }
}
