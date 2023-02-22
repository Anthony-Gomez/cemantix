import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

class TopWordsSorter {
    
    public static int ROUND = 0;
    
    public static int WORDS_SIZE;
    
    public static int EMPTY_WORDS_COUNT = 0;
    
    public static List<Integer> EMPTY_WORDS_LINES = new ArrayList<>();
    
    public static void main(final String... args) {
        final ZonedDateTime start = ZonedDateTime.now();
        if (args.length != 1) {
            throw new IllegalArgumentException("Argument [0] invalid : must only be the top-words.txt file path.");
        }
        sortAndPrintWordsByMostFrequent(countWords(getWords(loadFile(args[0]))));
        printInfo(start);
    }
    
    public static void printInfo(final ZonedDateTime start) {
        final String lineWording = EMPTY_WORDS_COUNT > 1 ? "lines " : "line ";
        final String lineInfo = EMPTY_WORDS_COUNT > 0 ? "\n" + EMPTY_WORDS_COUNT + " empty at " + lineWording + EMPTY_WORDS_LINES : "";
        System.out.printf("\nMost frequent words from %d rounds and %d words.%s",
                ROUND, WORDS_SIZE, lineInfo);
        System.out.print("\nTimexec : " + Duration.between(start, ZonedDateTime.now()).toMillis() + " ms");
    }
    
    public static FileInputStream loadFile(final String pathSource) {
        final FileInputStream file;
        try {
            file = new FileInputStream(pathSource);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
    
    public static List<String> getWords(final FileInputStream file) {
        final List<String> words = new ArrayList<>();
        // read file
        try (final Scanner scanner = new Scanner(file)) {
            int line = 1;
            // iterate all lines
            while (scanner.hasNextLine()) {
                final String word = scanner.nextLine();
                // ignore delimiter
                if ("#".equals(word)) {
                    ROUND++;
                } else if (word.isEmpty()) {
                    EMPTY_WORDS_COUNT++;
                    EMPTY_WORDS_LINES.add(line);
                } else {
                    words.add(word);
                }
                line++;
            }
            WORDS_SIZE = words.size();
        }
        return words;
    }
    
    public static Map<String, Long> countWords(final List<String> words) {
        return words.stream().collect(
                Collectors.groupingBy(
                        Function.identity(),
                        HashMap::new,
                        Collectors.counting()
                ));
    }
    
    public static void sortAndPrintWordsByMostFrequent(final Map<String, Long> wordsCounted) {
        wordsCounted.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(word -> System.out.println(word.getKey() + ": " + word.getValue()));
    }
}
