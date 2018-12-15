package ua.kpi.restaurants.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.Config;
import ua.kpi.restaurants.logic.common.algorithms.BKTreeSet;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;
import ua.kpi.restaurants.logic.strategies.preprocessing.Preprocessor;
import ua.kpi.restaurants.logic.strategies.preprocessing.QueryPreprocessor;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.QueryDenoiser;
import ua.kpi.restaurants.logic.common.utils.Serializer;
import ua.kpi.restaurants.logic.common.utils.metrics.Levenstein;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.representation.Location;
import ua.kpi.restaurants.logic.representation.Restaurant;

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

public final class DataBase implements Serializable {
  private static final long serialVersionUID = -7408309477031815349L;

  private final Set<String> stopWords;
  private final Map<String, Set<Dish>> index;
  private final SimilaritySet<String> similarities;

  private static final class InstanceHolder {
    private static final DataBase ourInstance;

    static {
      try {
        String db = Config.getInstance().getProperty("ua.kpi.restaurants.Config.dataBase");

        if (Files.exists(Paths.get(db))) {
          ourInstance = (DataBase) Serializer.deserializeNative(db);
        } else {
          String stopWords = Config.getInstance().getProperty("ua.kpi.restaurants.Config.stopWords");
          String dataSet = Config.getInstance().getProperty("ua.kpi.restaurants.Config.dataSet");
          String delimiter = Config.getInstance().getProperty("ua.kpi.restaurants.Config.dataSet.delimiter");

          ourInstance = new DataBase(stopWords, dataSet, delimiter);
          Serializer.serializeNative(db, ourInstance);
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
    index = Collections.unmodifiableMap(processDataSet(dataSetPath, delimiters));
    similarities = new BKTreeSet(new Levenstein());
    similarities.addAll(index.keySet());
  }

  private static Set<String> processStopWords(String filename) throws IOException {
    Set<String> words = new HashSet<>();

    try (Scanner scanner = new Scanner(new FileInputStream(filename), "utf-8")) {
      while (scanner.hasNext()) {
        words.add(scanner.next());
      }
    }

    return words;
  }

  private Map<String, Set<Dish>> processDataSet(String filename, String sep) throws IOException {
    Map<String, Restaurant> map = new HashMap<>();
    Map<String, Set<Dish>> dishes = new HashMap<>();
    Pattern pattern = Pattern.compile(sep);
    LanguageProperties properties = Config.getInstance().getLanguageProperties();
    Preprocessor.Builder builder = new QueryPreprocessor.Builder()
        .setDenoiser(new QueryDenoiser(stopWords))
        .setStemmer(properties.getStemmer())
        .addFilter(properties.getWordConstraint());

    try (Scanner scanner = new Scanner(new FileInputStream(filename), "utf-8")) {
      while (scanner.hasNextLine()) {
        String[] line = pattern.split(scanner.nextLine(), -1);

        Location location = properties.parseLocation(line);
        Restaurant restaurant = properties.parseRestaurant(line, location);
        Restaurant current = map.putIfAbsent(restaurant.getName(), restaurant);

        Dish dish = properties.parseDish(line, current != null ? current : restaurant);
        for (String word : builder.build(dish.getDescription().trim().toLowerCase())) {
          dishes.computeIfAbsent(word, k -> new HashSet<>()).add(dish);
        }
      }
    }

    dishes.entrySet().removeIf(properties.getDataConstraint().negate());
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
