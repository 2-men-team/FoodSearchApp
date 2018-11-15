package project.logic;

import org.jetbrains.annotations.NotNull;
import project.logic.common.algorithms.BKTreeSet;
import project.logic.common.algorithms.SimilaritySet;
import project.logic.common.utils.Denoiser;
import project.logic.common.utils.Serializer;
import project.logic.common.utils.metrics.Levenstein;
import project.logic.representation.Dish;
import project.logic.representation.Location;
import project.logic.representation.Restaurant;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DataBase implements Serializable {
    private static final long serialVersionUID = 8807060305647358996L;

    private final Set<String> stopWords;
    private final Map<String, Set<Dish>> index;
    private final SimilaritySet<String> similarities;

    private static final class InstanceHolder {
        private static final DataBase ourInstance;

        static {
            DataBase temp = null;

            try {
                if (Files.exists(Paths.get(Config.DB_PATH)))
                    temp = (DataBase) Serializer.deserialize(Config.DB_PATH);
                else {
                    temp = new DataBase();
                    Serializer.serialize(Config.DB_PATH, temp);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            }

            ourInstance = Objects.requireNonNull(temp); // should not be null
        }
    }

    public static DataBase getInstance() {
        return InstanceHolder.ourInstance;
    }

    private DataBase() throws IOException {
        stopWords = Collections.unmodifiableSet(processStopWords(Config.STOP_WORDS_PATH));
        index     = Collections.unmodifiableMap(processDataSet(Config.DATA_SET_PATH, ","));
        similarities = index.keySet().stream()
                .collect(Collectors.toCollection(() -> new BKTreeSet(new Levenstein())));
    }

    private Set<String> processStopWords(String filename) throws IOException {
        return Stream.of(Files.lines(Paths.get(filename)))
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
    }

    private Map<String, Set<Dish>> processDataSet(String filename, String separator) throws IOException {
        Map<String, Restaurant> map   = new HashMap<>();
        Map<String, Set<Dish>> dishes = new HashMap<>();
        Pattern pattern = Pattern.compile(separator);
        Denoiser denoiser = new Denoiser(stopWords);

        Stream.of(Files.lines(Paths.get(filename)))
                .flatMap(Function.identity())
                .map(s -> pattern.split(s, -1))
                .forEach(line -> {
                    String name = line[0];
                    if (!map.containsKey(name)) {
                        String description = line[1];
                        Location loc;
                        try {
                            double lat = Double.parseDouble(line[2]);
                            double lon = Double.parseDouble(line[3]);
                            loc = new Location(lat, lon, description);
                        } catch (NumberFormatException e) {
                            loc = Location.NONE;
                        }

                        map.put(name, new Restaurant(name, description, loc));
                    }

                    double price;
                    try {
                        price = Double.parseDouble(line[5]);
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                        price = Double.NaN;
                    }

                    List<String> cleared = denoiser.clear(line[4].trim().toLowerCase());
                    Dish dish = new Dish(String.join(" ", cleared), map.get(name), price);
                    for (String word : cleared) {
                        if (word.length() == 0) continue; // bad dataset
                        if (!dishes.containsKey(word)) dishes.put(word, new HashSet<>());
                        dishes.get(word).add(dish);
                    }
                });

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
}
