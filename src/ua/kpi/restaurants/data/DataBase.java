package ua.kpi.restaurants.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.common.algorithms.BKTreeSet;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;
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

/**
 * Provides access to all the data used by the application. It follows {@code Singleton} pattern.
 *
 * It uses {@link Config#getProperty(String)} to get access to properties needed.
 *
 * {@code DataBase} uses the following properties:
 * <ul>
 *   <li>{@code ua.kpi.restaurants.data.Config.stopWords} - to get access to path to file containing stop words</li>
 *   <li>{@code ua.kpi.restaurants.data.Config.dataSet} - to get access to path to file containing data set</li>
 *   <li>{@code ua.kpi.restaurants.data.Config.dataBase} - to get access to path to file where data base should be serialized</li>
 *   <li>{@code ua.kpi.restaurants.data.Config.dataSet.delimiter} - to get access to data set delimiter</li>
 * </ul>
 *
 * If {@code ua.kpi.restaurants.data.Config.dataBase} contains no file, data base is initialized using properties defined
 * above and serialized to that path. If that property has an appropriate file, it is deserialized and used as a data base instance.
 * If deserialization is not possible, exception is thrown.
 *
 * It uses {@link LanguageProperties} instance from {@link Config} class.
 *
 * Main {@code index} contains mappings from words to {@link Dish} instances that have that word in their description.
 * Dish descriptions are preprocessed according to the rules defined by {@link QueryPreprocessor}.
 * The following preprocessing routines are set explicitly:
 * <ul>
 *   <li>{@link QueryDenoiser}</li>
 *   <li>stemmer from {@link LanguageProperties#getStemmer()}</li>
 *   <li>filter from {@link LanguageProperties#getWordConstraint()}</li>
 * </ul>
 *
 * The final data is cleaned using the rules from {@link LanguageProperties#getDataConstraint()}.
 *
 * Similarities are initialized using {@link BKTreeSet} with {@link Levenstein} metric using keys from {@code index}.
 *
 * @see Config
 * @see LanguageProperties
 * @see BKTreeSet
 * @see Levenstein
 */
public final class DataBase implements Serializable {
  private static final long serialVersionUID = -7408309477031815349L;

  private final Set<String> stopWords;
  private final Map<String, Set<Dish>> index;
  private final SimilaritySet<String> similarities;

  private static final class InstanceHolder {
    private static final DataBase ourInstance;

    static {
      try {
        String db = Config.getInstance().getProperty("ua.kpi.restaurants.data.Config.dataBase");

        if (Files.exists(Paths.get(db))) {
          ourInstance = (DataBase) Serializer.deserializeNative(db);
        } else {
          String stopWords = Config.getInstance().getProperty("ua.kpi.restaurants.data.Config.stopWords");
          String dataSet = Config.getInstance().getProperty("ua.kpi.restaurants.data.Config.dataSet");
          String delimiter = Config.getInstance().getProperty("ua.kpi.restaurants.data.Config.dataSet.delimiter");

          ourInstance = new DataBase(stopWords, dataSet, delimiter);
          Serializer.serializeNative(db, ourInstance);
        }
      } catch (IOException | ClassNotFoundException e) {
        throw new ProjectRuntimeException("Error while loading database", e);
      }
    }
  }

  /**
   * Provides access to current {@code DataBase} instance.
   * Initializes it in the case of first call.
   * @return {@code DataBase} instance
   * @throws ProjectRuntimeException if initialization failed
   */
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
    LanguageProperties properties = Config.getInstance().getLanguage().getProperties();
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
          dishes.computeIfAbsent(word, key -> new HashSet<>()).add(dish);
        }
      }
    }

    dishes.entrySet().removeIf(properties.getDataConstraint().negate());
    return dishes;
  }

  /**
   * Provides access to stop words set.
   * @return {@link Set} containing stop words
   */
  @NotNull
  public Set<String> getStopWords() {
    return stopWords;
  }

  /**
   * Provides access to the set of similar words.
   * @return {@link SimilaritySet} instance of word similarities
   */
  @NotNull
  public SimilaritySet<String> getSimilarities() {
    return similarities;
  }

  /**
   * Provides access to data available.
   * @return {@link Map} containing mappings from words to corresponding dishes
   */
  @NotNull
  public Map<String, Set<Dish>> getData() {
    return index;
  }
}
