package project;

import org.jetbrains.annotations.NotNull;
import project.logic.common.algorithms.BKTreeSet;
import project.logic.common.algorithms.SimilaritySet;
import project.logic.strategies.preprocessing.Preprocessor;
import project.logic.strategies.preprocessing.QueryPreprocessor;
import project.logic.strategies.preprocessing.routines.QueryDenoiser;
import project.logic.common.utils.Serializer;
import project.logic.common.utils.metrics.Levenstein;
import project.logic.strategies.preprocessing.routines.Stemmer;
import project.logic.representation.Dish;
import project.logic.representation.Location;
import project.logic.representation.Restaurant;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DataBase implements Serializable {
    private static final long serialVersionUID = -160885989201790372L;

    private final Set<String> stopWords;
    private final Map<String, Set<Dish>> index;
    private final SimilaritySet<String> similarities;
    private final Map<String, Integer> frequencies;

    private static final class InstanceHolder {
        private static final DataBase ourInstance;

        static {
            try {
                if (Files.exists(Paths.get(Config.DB_PATH))) {
                    ourInstance = (DataBase) Serializer.deserializeNative(Config.DB_PATH);
                } else {
                    ourInstance = new DataBase(Config.STOP_WORDS_PATH, Config.DATA_SET_PATH, "\\|");
                    Serializer.serializeNative(Config.DB_PATH, ourInstance);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Error while loading database", e);
            }
        }
    }

    public static DataBase getInstance() {
        return InstanceHolder.ourInstance;
    }

    private DataBase(String stopWordsPath, String dataSetPath, String delimiters) throws IOException {
        stopWords = Collections.unmodifiableSet(processStopWords(stopWordsPath));
        index     = Collections.unmodifiableMap(processDataSet(dataSetPath, delimiters));
        similarities = new BKTreeSet(new Levenstein());
        similarities.addAll(index.keySet());
        frequencies = Collections.unmodifiableMap(index.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size())));
    }

    private static Set<String> processStopWords(String filename) throws IOException {
        Set<String> words = new HashSet<>();

        try (Scanner scanner = new Scanner(new FileInputStream(filename))) {
            while (scanner.hasNext()) {
                words.add(scanner.next());
            }
        }

        return words;
    }

    private Map<String, Set<Dish>> processDataSet(String filename, String separator) throws IOException {
        Map<String, Restaurant> map   = new HashMap<>();
        Map<String, Set<Dish>> dishes = new HashMap<>();
        Pattern pattern = Pattern.compile(separator);
        Pattern wordPattern = Pattern.compile("[a-zA-Z]+");
        Preprocessor.Builder builder = new QueryPreprocessor.Builder()
                .setDenoiser(new QueryDenoiser(stopWords))
                .setStemmer(Stemmer.ENGLISH);

        try (Scanner scanner = new Scanner(new FileInputStream(filename))) {
            while (scanner.hasNextLine()) {
                String[] line = pattern.split(scanner.nextLine(), -1);

                String name = line[0];
                if (!map.containsKey(name)) {
                    String description = line[1];
                    Location location = Location.valueOf(line[2], line[3], description);
                    map.put(name, new Restaurant(name, description, location));
                }

                double price;
                try {
                    price = Double.parseDouble(line[5]);
                } catch (NumberFormatException e) {
                    price = Double.NaN;
                }

                Dish dish = new Dish(line[4], map.get(name), price);
                builder.build(line[4].trim().toLowerCase())
                        .asStream()
                        .filter(word -> word.length() > 2)
                        .filter(wordPattern.asPredicate())
                        .forEach(word -> dishes.computeIfAbsent(word, k -> new HashSet<>()).add(dish));
            }
        }

        dishes.entrySet().removeIf(entry -> entry.getValue().size() < 150);
        return dishes;
    }

    @NotNull
    public Set<String> getStopWords() {
        return stopWords;
    }

    @NotNull
    public SimilaritySet<String> getSimilarities() {
        return similarities;
    }

    @NotNull
    public Map<String, Set<Dish>> getData() {
        return index;
    }

    @NotNull
    public Map<String, Integer> getFrequencies() {
        return frequencies;
    }
}
